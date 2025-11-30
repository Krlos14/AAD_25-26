package com.ciglgal1409.AAD.repository;

import com.ciglgal1409.AAD.model.Student;
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
public class StudentRepository implements CrudRepository<Student> {

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

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Student insert(Student s) {
        if (s == null) throw new IllegalArgumentException("Los estudianntes no pueden ser nulll");
        KeyHolder key = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT, new String[] {"id_alumno"});
            ps.setString(1, s.getNif());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());
            return ps;
        }, key);
        Number keys = key.getKey();
        if (keys != null) {
            s.setId(keys.intValue());
        }
        log.info("create OK: {}", s);
        return s;
    }

    @Override
    public List<Student> findAll() {
        List<Student> stu = jdbcTemplate.query(SQL_SELECT, (rs, rowNum) -> new Student(
                        rs.getInt("id_alumno"),
                        rs.getString("nif"),
                        rs.getString("nombre"),
                        rs.getString("email")
                )
        );
        log.info("Todos los estudiantes OK");
        return stu;
    }

    @Override
    public Student findById(int id) {
        List<Student> students = jdbcTemplate.query(SQL_SELECT_BY_ID, (rs, rowNum) -> new Student(
                        rs.getInt("id_alumno"),
                        rs.getString("nif"),
                        rs.getString("nombre"),
                        rs.getString("email")
                ), id
        );
        log.info("OK Estudiantes por id={}", id);
        return students.isEmpty() ? null : students.get(0);
    }

    @Override
    public Student update(Student stu) {
        int updated = jdbcTemplate.update(SQL_UPDATE, stu.getNif(), stu.getName(), stu.getEmail(), stu.getId());
        if (updated == 0) {
            throw new RuntimeException("Estudiante no encontrado por : id=" + stu.getId());
        }
        log.info("Actualizacion estudiantes  OK id={}", stu.getId());
        return stu;
    }

    @Override
    public boolean delete(int id) {
        int borra = jdbcTemplate.update(SQL_DELETE, id);
        log.info("Estudiante eliminado OK id={}", id);
        return borra > 0;
    }
}