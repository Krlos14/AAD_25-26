package com.ciglgal1409.AAD.repository;

import com.ciglgal1409.AAD.config.PostgresqlDriver;
import com.ciglgal1409.AAD.model.Enrollment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class EnrollementRepository {

    private static final String SQL_CREATE = """
            INSERT INTO matricula (id_alumno, id_modulo, fecha)
            VALUES (?, ?, ?)
            """;
    private static final String SQL_FINDALL = """
            SELECT *
            FROM matricula
            """;
    private static final String SQL_FINDBYSTUDENTID = """
            SELECT *
            FROM matricula
            WHERE id_alumno = ?
            """;
    private static final String SQL_DELETE = """
            DELETE FROM matricula
            WHERE id_alumno = ? AND id_modulo = ?
            """;

    private final PostgresqlDriver postgresqlDriver;

    public Enrollment insert(Enrollment e) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = postgresqlDriver.getConnection();  // ⚠️SIN try-with-resources
            ps = conn.prepareStatement(SQL_CREATE);

            ps.setInt(1, e.getStudentId());
            ps.setInt(2, e.getModuleId());
            ps.setDate(3, Date.valueOf(e.getDate()));
            ps.executeUpdate();

            log.info("create OK: {}", e);
            return e;
        } catch (SQLException er) {
            throw new RuntimeException("Error creating Enrollment", er);
        } finally {
            // Cerrar solo PreparedStatement, NO la Connection
            try {
                if (ps != null) ps.close();
            } catch (SQLException ex) {
                log.warn("Error closing PreparedStatement", ex);
            }
            // ⚠️NO cerrar conn aquí - se cerrará en commit()/rollback()
        }
    }

    public List<Enrollment> findAll() {
        List<Enrollment> enrollments = new ArrayList<>();
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FINDALL);
             ResultSet rs = ps.executeQuery())
        {
            while (rs.next())
            {
                Enrollment enrollment = mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading Student", e);
        }
        return enrollments;
    }
    public Enrollment findById(Integer id) {
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FINDBYSTUDENTID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading Student", e);
        }
    }
    public boolean delete(Integer id) {
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            int deleted = ps.executeUpdate();
            log.info("delete OK: {}", deleted > 0);
            return deleted > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Student", e);
        }
    }
    private Enrollment mapRow(ResultSet rs) throws SQLException {
        Enrollment en = new Enrollment();
        en.setStudentId(rs.getInt("id_alumno"));
        en.setModuleId(rs.getInt("id_module"));
        en.setDate(rs.getDate("fecha").toLocalDate());

        return en;
    }

}
