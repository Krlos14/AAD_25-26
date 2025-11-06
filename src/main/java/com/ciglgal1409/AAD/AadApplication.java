package com.ciglgal1409.AAD;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class AadApplication implements CommandLineRunner {
    private final PostgresqlDriver postgresqlDriver;

    public static void main(String[] args) {
        SpringApplication.run(AadApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Testing JDBC connection...");
        try (Connection conn = postgresqlDriver.getConnection()) {
            log.info("Connection successful: {}",
                    conn.getMetaData().getURL());
            log.info("Database: {}",
                    conn.getMetaData().getDatabaseProductName());
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }

    @Bean
    CommandLineRunner testRepo(com.ciglgal1409.AAD.repository.StudentJdbcRepository repo) {
        return args -> {
            // CREATE
            var s = new com.ciglgal1409.AAD.model.Student(
                    null, "Lucia", "Martinez",
                    java.time.LocalDate.of(2004, 5, 10), 8.7
            );
            s = repo.create(s);

            // READ
            var found = repo.read(new com.ciglgal1409.AAD.model.Student(s.getId(), null, null, null, null));
            System.out.println("Read: " + found);

            // UPDATE
            found.setAverageGrade(9.2);
            repo.update(found);

            // DELETE
            repo.delete(found);
        };
    }
}
