package com.ciglgal1409.AAD.repository;

import com.ciglgal1409.AAD.model.Enrollment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class EnrollementRepository {

    private static final String SQL_CREATE = """
            INSERT INTO matricula (id_alumno, id_modulo, fecha)
            VALUES (?, ?, ?)
            """;
    private static final String SQL_FIND_ALL = """
            SELECT *
            FROM matricula
            """;
    private static final String SQL_FIND_BY_STUDENTID = """
            SELECT *
            FROM matricula
            WHERE id_alumno = ?
            """;
    private static final String SQL_DELETE = """
            DELETE FROM matricula
            WHERE id_alumno = ? AND id_modulo = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    public Enrollment insert(Enrollment e) {
        log.info("Creating enrollment - Student ID: {}, Module ID: {}, Date: {}",
                e.getStudentId(), e.getModuleId(), e.getDate());

        jdbcTemplate.update(SQL_CREATE, e.getStudentId(), e.getModuleId(),
                e.getDate());

        log.info("Enrollment created successfully");
        return e;
    }


    public List<Enrollment> findAll() {
        log.info("Finding all enrollments");
        List<Enrollment> e = jdbcTemplate.query(SQL_FIND_ALL, (rs, rowNum) -> new Enrollment(
                rs.getInt("id_alumno"),
                rs.getInt("id_modulo"),
                rs.getDate("fecha").toLocalDate()
                )
        );
        log.info("FindAll enrollments OK");
        return e;
    }
    public List<Enrollment> findByStudent(int studentId) {
        List<Enrollment> enrollments = jdbcTemplate.query(SQL_FIND_BY_STUDENTID, (rs, rowNum) -> new Enrollment(
                        rs.getInt("id_alumno"),
                        rs.getInt("id_modulo"),
                        rs.getDate("fecha").toLocalDate()
                ),
                studentId);

        log.info("FindByStudent OK student ID: {}", studentId);
        return enrollments;
    }

    public boolean delete(int studentId, int moduleId) {
        log.info("Borrando Matricula - Student Id: {}, Module Id: {}", studentId, moduleId);

        int deleted = jdbcTemplate.update(SQL_DELETE, studentId, moduleId);
        boolean pass = deleted > 0;

        log.info("Matriculada Borrada {} - Student ID: {}, Module ID: {}",
                pass ? "SI" : "NO", studentId, moduleId);
        return pass;
    }
}
