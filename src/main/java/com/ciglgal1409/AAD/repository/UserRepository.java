package com.ciglgal1409.AAD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ciglgal1409.AAD.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public List<User> findByEmail(String email);
    public List<User> findByNameAndEmail(String name,String email);
}
