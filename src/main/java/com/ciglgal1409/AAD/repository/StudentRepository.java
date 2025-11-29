package com.ciglgal1409.AAD.repository;

import com.ciglgal1409.AAD.config.PostgresqlDriver;
import com.ciglgal1409.AAD.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class StudentRepository implements CustomService<Student> {

    private static final String SQL_INSERT = """
            INSERT INTO alumno (nif, nombre, email)
            VALUES ( ?, ?, ?)
            """;

    private static final String SQL_SELECT = """
            SELECT *
            FROM alumno
            """;

    private static final String SQL_SELECT_BY_ID = """
            SELECT *
            FROM alumno
            WHERE id_alumno = ?
            """;

    private static final String SQL_UPDATE = """
            UPDATE alumno
            SET nif = ?, nombre = ?, email = ?
            WHERE id = ?
            """;

    private static final String SQL_DELETE = """
            DELETE FROM alumno
            WHERE id_alumno = ?
            """;

    private final PostgresqlDriver postgresqlDriver;


    @Override
    public Student insert(Student entity) {
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNif());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getEmail());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) entity.setId(keys.getInt(1));
            }
            log.info("create OK: {}", entity);
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Student", e);
        }
    }

    @Override
    public List<Student> findAll() {
       List<Student> students = new ArrayList<>();
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT);
             ResultSet rs = ps.executeQuery())
        {
           while (rs.next())
           {
               Student student = mapRow(rs);
           }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading Student", e);
        }
        return students;
    }
    @Override
    public Student findById(Integer id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = postgresqlDriver.getConnection();  // ⚠️SIN try-with-resources
            ps = conn.prepareStatement(SQL_SELECT_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Student s = mapRow(rs);
                log.info("findById OK: {}", s);
                return s;
            } else {
                log.info("findById NOOP for id={}", id);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding Student id=" + id, e);
        } finally {
            // Cerrar solo Statement y ResultSet, NO la Connection
            try { if (rs != null) rs.close(); } catch (SQLException e) { log.warn("Error closing RS", e); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { log.warn("Error closing PS", e); }
            // ⚠️NO cerrar conn aquí - se cerrará en commit()/rollback()
        }
    }

    @Override
    public Student update(Student entity) {
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, entity.getNif());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getEmail());
            ps.setInt(4, entity.getId());
            ps.executeUpdate();
            log.info("update OK: {}", entity);
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Student", e);
        }
    }

    @Override
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

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id_alumno"));
        s.setNif(rs.getString("nif"));
        s.setName(rs.getString("nombre"));
        s.setEmail(rs.getString("email"));
        return s;
    }
}