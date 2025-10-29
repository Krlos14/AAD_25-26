package com.ciglgal1409.AAD;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

@SpringBootApplication
@Slf4j
public class AadApplication implements CommandLineRunner {
    private static Scanner s = new Scanner(System.in);
    private static Charset charset = StandardCharsets.UTF_8;

    public static void main(String[] args) {
        SpringApplication.run(AadApplication.class, args);
    }

    public static void addEvent() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/app.log", true), charset))) {
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String dateFormate = sdf.format(now);

            log.info("Write the event message:");
            String event = s.nextLine();

            log.info("Date automatically set: {}", dateFormate);
            writer.write("[" + dateFormate + "] Usuario: " + event);
            writer.newLine();
        } catch (IOException e) {
            log.error("Error" + e.getMessage());
        }
    }

    public static void filterEvent() {
        log.info("Enter a date of the event for filter");
        String date = s.nextLine();

        try (BufferedReader rw = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/app.log"), charset))) {
            String line = "";
            // boolean find = false
            while ((line = rw.readLine()) != null)
                if (line.contains("[" + date)) {
                    log.info("Event found : {}", line);
                }
        } catch (IOException e) {
            log.error("Error" + e.getMessage());
        }
    }

    public static void configurateCodi() {

        log.info("Configuration default is: {} ", charset);
        log.info("Select a new configuration");
        log.info("1) UTF-8");
        log.info("2) ISO-8859-1");
        int opc = s.nextInt();
        switch (opc) {
            case 1:
                charset = StandardCharsets.UTF_8;
                log.info("Select a new charset: {}", charset);
                break;
            case 2:
                charset = StandardCharsets.ISO_8859_1;
                break;
            default:
                log.info("Invalid option");
                log.info("Select a new charset: {}", charset);
                break;
        }
    }

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            int opc;
            log.info("Select a option");
            log.info("1.Add event");
            log.info("2.Filter envents");
            log.info("3.Configurate codificate");
            log.info("4.Exit");
            opc = s.nextInt();
            String vaciador = s.nextLine();
            switch (opc) {
                case 1:
                    log.info("Option 1 selected");
                    addEvent();
                    break;
                case 2:
                    log.info("Option 2 selected");
                    filterEvent();
                    break;
                case 3:
                    log.info("Option 3 selected");
                    configurateCodi();
                    break;
                case 4:
                    log.info("Bye bye");
                    System.exit(0);
                default:
                    log.info("Invalid option");
                    break;
            }
        }
    }
}