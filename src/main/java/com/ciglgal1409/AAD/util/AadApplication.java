package com.ciglgal1409.AAD.util;

import com.ciglgal1409.AAD.model.student;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class AadApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AadApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<student> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/students.csv"))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) { // saltar la primera línea
                    header = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                student s = new student(); //instacia student
                s.setId(Integer.parseInt(parts[0].trim())); //s.set--- sirve para darle a id, name y nota un valor o nombre
                s.setName(parts[1].trim());
                s.setNota(Double.parseDouble(parts[2].trim()));
                list.add(s);//Añade el estudiante leido a la lista (array)
            }
        }

        // Uso de métodos para crear los archivos .json y .xml
        writeJSON(list, "src/main/resources/students.json");
        writeXML(list, "src/main/resources/students.xml");
    }

    // Convertir lista a JSON
    private void writeJSON(List<student> list, String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper(); //Instancia para json
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), list);//escribe en un archivo .json guardado en list y con el PrettyPrinter para que se vea mejor
    }

    // Convertir lista a XML
    private void writeXML(List<student> students, String path) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();//Instancia para Xml
        xmlMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), students);//escribe en un archivo .xml guardado en list y con el PrettyPrinter para que se vea mejor
    }
}
