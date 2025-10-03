package com.ciglgal1409.AAD;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
@SpringBootApplication
@Slf4j
public class ACT1_11 implements CommandLineRunner
{

    public static void main(String[] args)
    {
        SpringApplication.run(AadApplication.class, args);
    }

    @Override
    public void run ( String... args ) throws Exception
    {

        int bucle= 0;
        Scanner s = new Scanner(System.in);
        Path ruta; //Variable creada para la ruta del directorio

        log.info("¿Que directorio quieres?");
        ruta = Paths.get(s.nextLine());

        File ruta2 = new File(ruta.toString()); //Clona ruta en ruta2 pero lo convierte en file2
        if (Files.exists(ruta) && Files.isDirectory(ruta)) {
            File[] archivos = ruta2.listFiles();

            if (archivos != null) {
                for (File archivo : archivos) {
                    if (archivo.isDirectory()) {
                        System.out.println("[DIR]  " + archivo.getName());
                    } else {
                        long espacio = archivo.length(); //Carga el almacenamiento del archivo en espacio para luego mostrarlo en pantalla
                        long ultimaModificacion = archivo.lastModified();
                        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(ultimaModificacion));
                        log.info("[FILE] " + archivo.getName() +
                                " | " + espacio + " bytes" +
                                " | modificado: " + fecha);
                    }
                }
            }
        } else {
            System.out.println("La ruta no es un directorio válido.");
        }

        while((bucle <= 4))
        { //Bucle hecho para poder usar el menú

            log.info ("1)Crear nuevo fichero");
            log.info ("2)Mover fichero de ubicación");
            log.info ("3)Borrar un fichero ");

            bucle = s.nextInt();
            s.nextLine();

            switch(bucle)
            {
                case 1:
                    log.info ("¿Que nombre del fichero quieres?");
                    String nombre = s.nextLine();

                    log.info ("¿Que directorio lo creamos?");
                    ruta = Paths.get(s.nextLine());
                    Path nuevoFichero = ruta.resolve(nombre); //Junta el directorio con el nombre de fichero
                    try
                    {
                        if (!Files.exists(nuevoFichero))//Comprueba la existencia del fichero
                        {
                           Files.createDirectories(nuevoFichero);
                           log.info(("Fichero creado "));
                        } else {
                            log.warn("El fichero ya existe."); //AVISO de que ya existe
                        }
                    } catch (IOException e)
                      {
                            log.warn("Error al cargar fichero." + e.getMessage());
                      }
                    break;
                case 2:

                    log.info("¿De que directorio quieres moverlo?");
                    ruta = Paths.get(s.nextLine());
                    log.info("¿A que directorio movemos el archivo?");
                    Path ruta3 = Paths.get(s.nextLine());

                    // Para poner el mismo nombre de archivo en la nueva ruta
                    if (Files.isDirectory(ruta3)) {
                        ruta3 = ruta3.resolve(ruta.getFileName());
                    }
                  try
                  {
                      Files.move(ruta,ruta3); //Con este comando movemos el archivo deseado del directorio "ruta" a la que quiera el usaurio
                  } catch (IOException e)
                      {
                          log.warn("Error al mover fichero." + e.getMessage());
                      }
                    break;
                  case 3:
                      try
                  {
                      log.info("Que archivo deseas eliminar");
                      ruta = Paths.get(s.nextLine());
                      Files.deleteIfExists(ruta);
                  } catch (IOException e)
                      {
                          log.warn("Error al eliminar fichero." + e.getMessage());
                      }
                      break;
            }

        }

    }
}

