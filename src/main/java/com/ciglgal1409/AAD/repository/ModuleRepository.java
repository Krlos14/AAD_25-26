package com.ciglgal1409.AAD.repository;

import com.ciglgal1409.AAD.config.PostgresqlDriver;
import com.ciglgal1409.AAD.model.Module;
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
public class ModuleRepository implements CustomService<Module> {

    private static final String SQL_INSERT = """
            INSERT INTO modulo (codigo, nombre, horas)
            VALUES (?, ?, ?)
            """;

    private static final String SQL_SELECT = """
            SELECT *
            FROM modulo
            """;

    private static final String SQL_SELECT_BY_ID = """
            SELECT *
            FROM modulo
            WHERE id_modulo = ?
            """;

    private static final String SQL_UPDATE = """
            UPDATE modulo
            SET codigo = ?, nombre = ?, horas = ?
            WHERE id = ?
            """;

    private static final String SQL_DELETE = """
            DELETE FROM moodulo
            WHERE id_modulo = ?
            """;

    private final PostgresqlDriver postgresqlDriver;

   

    @Override
    public Module insert(Module entity) {
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getCode());
            ps.setString(2, entity.getName());
            ps.setInt(3, entity.getHours());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) entity.setId(keys.getInt(1));
            }
            log.info("create OK: {}", entity);
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error creatingModule", e);
        }
    }

    @Override
    public List<Module> findAll() {
       List<Module> modules = new ArrayList<>();

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT);
             ResultSet rs = ps.executeQuery()) {
           while (rs.next()) {
               Module module = mapRow(rs);
           }
        } catch (SQLException e) {
            throw new RuntimeException("Error readingModule", e);
        }
        return modules;
    }
    public Module findById(Integer id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = postgresqlDriver.getConnection();  // ⚠️
            ps = conn.prepareStatement(SQL_SELECT_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Module s = mapRow(rs);
                log.info("findById OK: {}", s);
                return s;
            } else {
                log.info("findById NOOP for id={}", id);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding Module id=" + id, e);
        } finally {
            // Cerrar solo Statement y ResultSet, NO la Connection
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                log.warn("Error closing ResultSet", e);
            }
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                log.warn("Error closing PreparedStatement", e);
            }
            // ⚠️NO cerrar conn aquí - se cerrará en commit()/rollback()
        }
    }

    @Override
    public Module update(Module entity) {
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, entity.getCode());
            ps.setString(2, entity.getName());
            ps.setInt(3, entity.getHours());
            ps.setInt(4, entity.getId());
            ps.executeUpdate();
            log.info("update OK: {}", entity);
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error updatingModule", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id );
            int deleted = ps.executeUpdate();
            log.info("delete OK: {}", deleted > 0);
            return deleted > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deletingModule", e);
        }
    }

    private Module mapRow(ResultSet rs) throws SQLException {
       Module m = new Module();
        m.setId(rs.getInt("id_modulo"));
        m.setCode(rs.getString("codigo"));
        m.setName(rs.getString("nombre"));
        m.setHours(rs.getInt("horas"));
        return m;
    }
}
