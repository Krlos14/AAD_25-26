package com.ciglgal1409.AAD.application;

import com.ciglgal1409.AAD.model.Enrollment;
import com.ciglgal1409.AAD.model.Student;
import com.ciglgal1409.AAD.repository.EnrollmentRepository;
import com.ciglgal1409.AAD.repository.ModuleRepository;
import com.ciglgal1409.AAD.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ManagementService {

    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public com.ciglgal1409.AAD.model.Module createModule(com.ciglgal1409.AAD.model.Module module) {
        return moduleRepository.save(module);
    }

    @Transactional
    public Enrollment enrollStudentInModule(Long studentId, Long moduleId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));


        com.ciglgal1409.AAD.model.Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setModule(module);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setFinalGrade(0.0);

        return enrollmentRepository.save(enrollment);
    }

    @Transactional(readOnly = true)
    public int countEnrollments(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        return student.getEnrollments().size();
    }
}