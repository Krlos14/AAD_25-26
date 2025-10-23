package com.ciglgal1409.AAD;

import com.ciglgal1409.AAD.model.modulo;
import com.ciglgal1409.AAD.model.student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Slf4j
public
class
AadApplication
        implements
        CommandLineRunner
{
    private final StudentCustomServices studentCustomServices;

    public static void main(String[] args)
    {
             SpringApplication.run(AadApplication.class,args);
    }
@Override
public void run ( String... args ) throws Exception
    {
        student student = new student("Paco", "Aguilera","29623045T", "2ºDAM");
        student student1 = new student("Juan", "Mayazno","6264306F","1ºAsir");
        modulo mod = new modulo("1245151", "Base de datos")
        List <modulo> modulos = List.of(mod);
        StudentCustomServices.createStudent(student, mod);
    }
}
