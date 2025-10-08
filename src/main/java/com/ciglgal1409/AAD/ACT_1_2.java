package com.ciglgal1409.AAD;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Scanner;

@SpringBootApplication
@Slf4j
public class ACT_1_2 implements CommandLineRunner {

   
    private static final int nombrelim = 20; // 20 caracteres fijos para el nombre
    private static final int registro = Integer.BYTES + (Character.BYTES * nombrelim) + Double.BYTES; //Hace que la variable de los registros sea siempre 52 con las variables que se usan
    private static final Path ruta = Path.of("alumnos.dat"); // fichero-ruta en binario

    static final Scanner s = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(ACT_1_2.class, args);
    }

    @Override
    public void run(String... args) {
        while (true) {
            log.info("\n--- Gestión de alumnado ({} bytes/registro) ---", registro);
            log.info("1) Añadir nuevo registro");
            log.info("2) Consultar alumno por posición");
            log.info("3) Modificar nota por posición");
            log.info("4) Salir");
            System.out.print("Elige opción: ");

            String op = s.nextLine().trim();

            try {
                switch (op) {
                    case "1":
                        anadirNuevo();
                        break;
                    case "2":
                        consultarAlumno();
                        break;
                    case "3":
                        modificarNotas();
                        break;
                    case "4":
                        log.info("Has salido del menú.");
                        return;
                    default:
                        log.info("Opción no válida.");
                }
            }  catch (NumberFormatException e) {
                log.warn("Entrada numérica inválida.");
            } catch (IOException e) {
                log.error("I/O Error: {}", e.getMessage());
            }
        }
    }

   
    static void anadirNuevo() throws IOException {// Inserta el alumno que se desea con sus datos en el fichero
        log.info("Ingresa el id del alumno:");
        int alumnoid = Integer.parseInt(s.nextLine().trim());

        log.info("Ingresa el nombre del alumno (máx 20 chars):");
        String nombre = s.nextLine();

        log.info("Ingresa la nota del alumno:");
        double nota = Double.parseDouble(s.nextLine().trim());

        try (RandomAccessFile raf = new RandomAccessFile(ruta.toFile(), "rw")) {
            raf.seek(raf.length());                // fin del fichero
            escribeRegistro(raf, alumnoid, nombre, nota);
        }

        log.info("Alumno insertado correctamente.");
    }


    static void consultarAlumno() throws IOException {// Consulta aleatoria por indice (0-based). Sin clase Alumno, imprime campos
        log.info("Posición (0-based): ");
        long index = Long.parseLong(s.nextLine().trim());

        try (RandomAccessFile raf = new RandomAccessFile(ruta.toFile(), "r")) {
            long total = raf.length() / registro;
            if (index < 0 || index >= total) {
                throw new IndexOutOfBoundsException("Índice fuera de rango. Total registros: " + total);
            }

            // Salta al inicio del registro
            raf.seek(index * registro);


            int id = raf.readInt();

            char[] nameChars = new char[nombrelim];
            for (int i = 0; i < nombrelim; i++) {
                nameChars[i] = raf.readChar();
            }
            String nombre = new String(nameChars).trim();

            double nota = raf.readDouble();


           log.info("ID: " + id);
           log.info("Nombre: " + nombre);
           log.info("Nota: " + nota);

        }
    }


    static void modificarNotas() throws IOException {// Modifica solo la nota (8 bytes) sin reescribir todo el registro
        log.info("Posición (0 es la posición inicial): ");
        long index = Long.parseLong(s.nextLine().trim());

        log.info("Nueva nota (double): ");
        double nuevaNota = Double.parseDouble(s.nextLine().trim());

        try (RandomAccessFile raf = new RandomAccessFile(ruta.toFile(), "rw")) {
            long total = raf.length() / registro;
            if (index < 0 || index >= total) {
                throw new IndexOutOfBoundsException("Índice fuera de rango. Total registros: " + total);
            }
            long base = index * registro;
            long offsetNota = base + Integer.BYTES + (long) Character.BYTES * nombrelim; // 4 + 40 = 44
            raf.seek(offsetNota);
            raf.writeDouble(nuevaNota); // solo 8 bytes
        }

        log.info("Nota actualizada.");
    }




    private static void escribeRegistro(RandomAccessFile raf, int id, String nombre, double nota) throws IOException { // Escribe el registro completo
        raf.writeInt(id); // 4 bytes

        // nombre fijo a 20 chars (relleno con espacios si es corto; truncado si es largo)
        char[] buffer = new char[nombrelim];
        String n = (nombre == null) ? "" : nombre;
        n = (n.length() > nombrelim) ? n.substring(0, nombrelim) : n;
        for (int i = 0; i < nombrelim; i++) {
            buffer[i] = (i < n.length()) ? n.charAt(i) : ' ';
        }
        for (char c : buffer) {
            raf.writeChar(c); // 2 bytes por char -> 40 bytes
        }

        raf.writeDouble(nota); // 8 bytes
    }
}
