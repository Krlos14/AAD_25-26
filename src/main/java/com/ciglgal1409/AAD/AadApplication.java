package com.ciglgal1409.AAD;

import com.ciglgal1409.AAD.application.ManagementService;
import com.ciglgal1409.AAD.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@Slf4j
public class AadApplication implements CommandLineRunner {

    private final ManagementService managementService;

    public AadApplication(ManagementService managementService) {
        this.managementService = managementService;
    }

    public static void main(String[] args) {
        SpringApplication.run(AadApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("--- INICIANDO PRUEBAS ---");

        Student miriam = new Student();
        miriam.setNif("66280457T");
        miriam.setName("Miriam");
        miriam.setEmail("miriam@g.educaand.es");
        miriam.setCourse("DAW");

        miriam = managementService.createStudent(miriam);
        log.info("Alumno guardado: {}", miriam);

        com.ciglgal1409.AAD.model.Module prog = new com.ciglgal1409.AAD.model.Module();
        prog.setCode("0485");
        prog.setName("Programación");
        prog.setHours(250);

        prog = managementService.createModule(prog);
        log.info("Módulo guardado: {}", prog);

        managementService.enrollStudentInModule(miriam.getId(), prog.getId());
        log.info("Matrícula creada correctamente.");

        log.warn("Lanzando error forzado para probar @Transactional...");
        throw new RuntimeException("Forzando rollback de la transacción");
    }
}