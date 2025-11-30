package com.ciglgal1409.AAD.repository;

import com.ciglgal1409.AAD.model.Module;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ModuleRepository implements CrudRepository<Module> {

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

    private final JdbcTemplate jdbcTemplate;



    @Override
    public Module insert(Module mod) {
        if (mod == null) throw new IllegalArgumentException("Student cannot be null");
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT, new String[] {"id_modulo"});
            ps.setString(1, mod.getCode());
            ps.setString(2, mod.getName());
            ps.setInt(3, mod.getHours());
            return ps;
        }, kh);
        Number key = kh.getKey();
        if (key != null) {
            mod.setId(key.intValue());
        }
        log.info("create OK: {}", mod);
        return mod;
    }

    @Override
    public List<Module> findAll() {
        log.info("Ejecutando findAll Modules");
        List<Module> mod = jdbcTemplate.query(
                SQL_SELECT,
                (rs, rowNum) -> new Module(
                        rs.getInt("id_modulo"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getInt("horas")
                )
        );
        log.info("FindAll Modules OK");
        return mod;
    }

    @Override
    public Module findById(int id) {
        List<Module> mod = jdbcTemplate.query(
                SQL_SELECT_BY_ID,
                (rs, rowNum) -> new Module(
                        rs.getInt("id_modulo"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getInt("horas")
                ),
                id
        );
        log.info("FindById Modules OK id={}", id);
        return mod.isEmpty() ? null : mod.get(0);
    }


    @Override
    public Module update(Module mod) {
        int updated = jdbcTemplate.update(SQL_UPDATE, mod.getCode(), mod.getName(), mod.getHours(), mod.getId());
        if (updated == 0)
        {
            throw new RuntimeException("Modulo no encontrado : id=" + mod.getId());
        }
        log.info("Modulo actualizado OK id={}", mod.getId());
        return mod;
    }

    @Override
    public boolean delete(int id) {
        int borra = jdbcTemplate.update(SQL_DELETE, id);
        log.info("Modulos eliminados OK id = {}", id);
        return borra > 0;
    }
}
