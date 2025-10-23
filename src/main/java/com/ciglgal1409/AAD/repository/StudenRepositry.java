package com.ciglgal1409.AAD.repository;

import com.ciglgal1409.AAD.model.student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j

public class StudenRepositry implements rootRepository<student> {


    /**
     * @param entity
     * @return
     */
    public static bolean create(student entity) {
        log.info("Insert" + entity.toString());
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public student update(student entity) {
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public student delete(student entity) {
        return null;
    }

    /**
     * @param entity
     */
    @Override
    public void read(student entity) {

    }
}
