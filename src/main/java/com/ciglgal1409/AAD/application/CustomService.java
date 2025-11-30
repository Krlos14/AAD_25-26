package com.ciglgal1409.AAD.application;

import com.ciglgal1409.AAD.model.Enrollment;
import com.ciglgal1409.AAD.model.Student;
import com.ciglgal1409.AAD.model.Module;

    public interface CustomService {
        Module createModule(Module module);
        Student createStudent(Student student) throws IllegalArgumentException;
        Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) throws Exception;
    }

