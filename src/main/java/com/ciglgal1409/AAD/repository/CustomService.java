package com.ciglgal1409.AAD.repository;

import com.ciglgal1409.AAD.model.Student;

import java.util.List;

public interface CustomService<T> {

    T insert(T entity);

    List<T> findAll();

    T findById(Integer id);

    T update(T entity);

    boolean delete(Integer id);
}
