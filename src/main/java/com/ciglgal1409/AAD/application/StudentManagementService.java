package com.ciglgal1409.AAD.application;

import com.ciglgal1409.AAD.config.PostgresqlDriver;
import com.ciglgal1409.AAD.model.Enrollment;
import com.ciglgal1409.AAD.model.Module;
import com.ciglgal1409.AAD.model.Student;
import com.ciglgal1409.AAD.repository.CustomService;
import com.ciglgal1409.AAD.repository.EnrollementRepository;
import com.ciglgal1409.AAD.repository.ModuleRepository;
import com.ciglgal1409.AAD.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentManagementService implements CustomService<Student> {

    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollementRepository enrollementRepository;
    private final PostgresqlDriver postgresqlDriver;

    @Override
    public Student insert(Student entity) {
        return createStudent(entity);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(Integer id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student update(Student entity) {
        return studentRepository.update(entity);
    }

    @Override
    public boolean delete(Integer id) {
        return studentRepository.delete(id);
    }



    /**
     * Crea un nuevo módulo. Si ya existe uno con el mismo código, devuelve el existente.
     */
    public Module createModule(Module module) {

        if (module == null) {
            throw new IllegalArgumentException("El módulo no puede ser null");
        }
        if (module.getCode() == null || module.getCode().isBlank()) {
            throw new IllegalArgumentException("El código del módulo es obligatorio");
        }

        List<Module> modules = moduleRepository.findAll();
        for (Module m : modules) {
            if (m.getCode().equalsIgnoreCase(module.getCode())) {
                log.info("Módulo ya existente con código {}. Devolviendo módulo existente.", module.getCode());
                return m;
            }
        }

        Module inserted = moduleRepository.insert(module);
        log.info("Módulo creado: {}", inserted);
        return inserted;
    }

    /**
     * Crea un nuevo estudiante validando campos obligatorios
     * y comprobando si ya existe (por NIF, por ejemplo).
     */
    public Student createStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("El estudiante no puede ser null");
        }
        if (student.getName() == null || student.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (student.getNif() == null || student.getNif().isBlank()) {
            throw new IllegalArgumentException("El NIF es obligatorio");
        }

        List<Student> students = studentRepository.findAll();
        for (Student s : students) {
            if (s.getNif().equalsIgnoreCase(student.getNif())) {
                log.info("Estudiante ya registrado con NIF {}. Se devuelve el existente.", student.getNif());
                return s; // <-- Se devuelve el que ya existe
            }
        }

        Student saved = studentRepository.insert(student);
        log.info("Estudiante creado: {}", saved);
        return saved;
    }

    /**
     * Matricula un estudiante en un módulo usando transacción manual con PostgresqlDriver.
     */
    public Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) {
        // Validar parámetros
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        if (moduleId == null) {
            throw new IllegalArgumentException("Module ID cannot be null");
        }

        System.out.println("Enrolling - Student ID: " + studentId + ", Module ID: " + moduleId);

        try {
            postgresqlDriver.beginTransaction();

            var student = studentRepository.findById(studentId);
            if (student == null) throw new IllegalArgumentException("Student not found: " + studentId);
            System.out.println("Found student: " + student.getId() + " - " + student.getName());

            var module = moduleRepository.findById(moduleId);
            if (module == null) throw new IllegalArgumentException("Module not found: " + moduleId);
            System.out.println("Found module: " + module.getId() + " - " + module.getName());

            Enrollment created = enrollementRepository.insert(new Enrollment(student.getId(), module.getId(), LocalDate.now()));
            System.out.println("Created enrollment: " + created.getStudentId() + " -> " + created.getModuleId());

            postgresqlDriver.commit();
            return created;

        } catch (Exception e) {
            postgresqlDriver.rollback();
            throw new RuntimeException("Error enrolling student in module", e);
        }
    }
    public int countEnrollments(int studentId) {
        try (Connection conn = postgresqlDriver.getConnection();
             CallableStatement cs = conn.prepareCall("{ ? = call count_enrollments(?) }")) {
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, studentId);
            cs.execute();
            return cs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Error count Enrollment", e);
        }
    }
}
