package com.ciglgal1409.AAD;

import com.ciglgal1409.AAD.application.StudentManagementService;
import com.ciglgal1409.AAD.config.PostgresqlDriver;
import com.ciglgal1409.AAD.model.Student;
import com.ciglgal1409.AAD.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.ciglgal1409.AAD.model.Module;
import java.sql.Connection;
import java.util.List;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class AadApplication implements CommandLineRunner {
    private final PostgresqlDriver postgresqlDriver;
    private final StudentRepository studentRepository;
    private final StudentManagementService studentManagementService;


    public static void main(String[] args) {
        SpringApplication.run(AadApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Student miriam = new Student(null, "66280457F", "Miriam", "miriam@g.educaand.et");
        Module programacion = new Module(null, "0425", "Programacion", 250);
        miriam = studentManagementService.createStudent(miriam);
        programacion = studentManagementService.createModule(programacion);
        int modulosMatriculados = studentManagementService.countEnrollments(miriam.getId());
        log.info("{} módulos matriculados para el alumno {}", modulosMatriculados, miriam.getName());
        studentManagementService.enrollStudentInModule(miriam.getId(), programacion.getId());
        studentRepository.delete(miriam.getId());

    }
}
