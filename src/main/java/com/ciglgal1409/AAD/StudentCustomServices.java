package com.ciglgal1409.AAD;

import com.ciglgal1409.AAD.model.student;
import com.ciglgal1409.AAD.repository.StudenRepositry;
import org.springframework.stereotype.Service;

@Service

public class StudentCustomServices implements CustomServices<student> {
    /**
     * @param entity
     * @return
     */
    @Override
    public boolean validar(student entity) {
        return entity.getDni().isBlank() && entity.getNombre().isBlank();
    }

    public bolean createStudent(student student) {
        if (validar(student)) {
            return StudenRepositry.create(student);

        }
        return false;
    }
}
