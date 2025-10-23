package com.ciglgal1409.AAD.repository;

public interface rootRepository<T> {

    T create(T entity);

    T update(T entity);

    T delete(T entity);

    void read(T entity);
}
