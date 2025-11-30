package com.ciglgal1409.AAD.application;

import com.ciglgal1409.AAD.model.Enrollment;
import com.ciglgal1409.AAD.model.Module;
import com.ciglgal1409.AAD.model.Student;
import com.ciglgal1409.AAD.repository.EnrollementRepository;
import com.ciglgal1409.AAD.repository.ModuleRepository;
import com.ciglgal1409.AAD.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentManagementService implements CustomService{

    private final JdbcTemplate jdbcTemplate;
    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollementRepository enrollementRepository;

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
    @Override
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
    @Transactional
    @Override
    public Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) {
        var student = studentRepository.findById(studentId);
        if (student == null) throw new IllegalArgumentException("Student not found: " + studentId);

        var module = moduleRepository.findById(moduleId);
        if (module == null) throw new IllegalArgumentException("Module not found: " + moduleId);

        return enrollementRepository.insert(
                new Enrollment(student.getId(), module.getId(), LocalDate.now())
        );
    }
    public int countEnrollments(int studentId) {
        log.info("Counting enrollments for student ID: {}", studentId);

        SimpleJdbcCall countEnrollmentsCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("count_enrollments");

        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_student_id", studentId);

        int count = countEnrollmentsCall.executeFunction(Integer.class, in);

        log.info("CountEnrollments OK - Student ID: {} has {} enrollments", studentId, count);
        return count;
    }
}
