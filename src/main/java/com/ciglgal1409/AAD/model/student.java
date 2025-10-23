package com.ciglgal1409.AAD.model;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Slf4j
@Data

public class student extends Persona {

    private String curso;

    public student(String nombre, String apellido, String dni, String curso) {
        super(nombre, apellido, dni);
        this.curso = curso;
    }

//    public String getCurso() {
//        return curso;
//    }
//
//    public void setCurso(String curso) {
//        this.curso = curso;
//    }


}

