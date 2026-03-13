package com.ciglgal1409.AAD.repository;

import com.ciglgal1409.AAD.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByNif(String nif);
}