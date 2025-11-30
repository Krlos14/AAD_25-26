### Gestión de Matrículas con Spring Boot y PostgreSQL (JDBC)
- Descripción General

  
  Este proyecto implementa un sistema de gestión de matrículas académicas utilizando:
  ```
  - Spring Boot (Java 17)
  - PostgreSQL en Docker
  - Acceso mediante JDBC 
  - Transacciones manuales
  - Funciones almacenadas en PostgreSQL
  ```
El objetivo de la práctica es demostrar el dominio de:

    - La creación y configuración de una base de datos mediante scripts SQL.
    - La conexión a PostgreSQL utilizando JDBC puro.
    - El diseño de repositorios con consultas SQL preparadas.
    - La gestión manual de transacciones (commit / rollback).
    - La ejecución de procedimientos y funciones almacenadas desde Java.

### Instrucciones de Ejecución
1.  Levantar PostgreSQL con Docker

    - Asegúrate de que el archivo docker-compose.yml esté ubicado en src/main/resources/.

   2. Contenido recomendado:
       ```
       services:
       postgres:
       image: postgres:15
       container_name: aad_postgres
       environment:
       POSTGRES_DB: aad_db
       POSTGRES_USER: postgres
       POSTGRES_PASSWORD: 1234
       ports:
      - "5432:5432"
          ```

3. Para iniciar PostgreSQL:
```
 Utilizamos el comando docker compose up -d en la carpeta 
```
4. Comprobar que está funcionando:
```
 Para comprobar su funcionamiento utilizamos el comando docker ps 
 en el cmd abierto desde la carpeta
```
5. Compilar y ejecutar el proyecto con Maven

- Desde la raíz del proyecto:

    - mvn clean package
    - mvn spring-boot:run

Asegúrate de que el archivo application.yml contenga los parámetros necesarios:
```
spring:
datasource:
url: jdbc:postgresql://localhost:5432/aad_db
username: postgres
password: 1234
driver-class-name: org.postgresql.Driver
```
6. Ejecutar el método run() proporcionado

La clase Application.java debe implementar CommandLineRunner e inyectar StudentManagementService.

Pega este código sin modificar:
```
@Override
public void run(String... args) throws Exception {
Student miriam = new Student(null, "66280457T", "Miriam",
"miriam@g.educaand.es", "DAW", List.of());
Module programacion = new Module(null, "0485", "Programación",
250);
     miriam = studentManagementService.crea
     teStudent(miriam);
    programacion = studentManagementService.createModule(programacion);
    
    int modulosMatriculados =
            studentManagementService.countEnrollments(miriam.getId());
    log.info("{} módulos matriculados para el alumno {}",
            modulosMatriculados, miriam.getName());

    studentManagementService.enrollStudentInModule(miriam.getId(),
            programacion.getId());

    studentRepository.delete(miriam.getId());
}
```

Si la configuración es correcta, la aplicación:

Inicializará la base de datos a partir de los scripts SQL.

Creará un estudiante, un módulo y una matrícula.

Ejecutará la función almacenada count_enrollments.

Mostrará logs en consola.

Persistirá la información en PostgreSQL.
```
️Estructura del Proyecto
src/main/java/com/<usuario>/aad/
├── config/
│   └── PostgresqlDriver.java
├── repository/
│   ├── StudentRepository.java
│   ├── ModuleRepository.java
│   └── EnrollmentRepository.java
├── model/
│   ├── Student.java
│   ├── Module.java
│   └── Enrollment.java
├── application/
│   └── StudentManagementService.java
└── Application.java

src/main/resources/
├── application.yml
├── docker-compose.yml
└── sql/
└── ddl/
├── 01_schema.sql
└── 02_procedures.sql
```
### Evidencias de Ejecución
Log obtenido al ejecutar la aplicación

A continuación se incluye la traza generada durante la ejecución del proyecto, usada como evidencia del correcto funcionamiento del sistema, incluyendo la inicialización de la base de datos, creación de entidades, ejecución de transacciones e invocación de funciones:
```
:: Spring Boot ::                (v3.5.6)

2025-11-30T12:30:45.265+01:00  INFO 72640 --- [           main] com.ciglgal1409.AAD.AadApplication       : Starting AadApplication using Java 21.0.5 with PID 72640 (C:\Users\Gao\IdeaProjects\AAD_25-26\target\classes started by Gao in C:\Users\Gao\IdeaProjects\AAD_25-26)
2025-11-30T12:30:45.268+01:00  INFO 72640 --- [           main] com.ciglgal1409.AAD.AadApplication       : No active profile set, falling back to 1 default profile: "default"
2025-11-30T12:30:46.296+01:00  INFO 72640 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2025-11-30T12:30:46.309+01:00  INFO 72640 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2025-11-30T12:30:46.310+01:00  INFO 72640 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.46]
2025-11-30T12:30:46.362+01:00  INFO 72640 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2025-11-30T12:30:46.362+01:00  INFO 72640 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1032 ms

2025-11-30T12:30:46.437+01:00  INFO 72640 --- [           main] c.c.AAD.config.PostgresqlDriver          : 🛠️ Initializing database...
2025-11-30T12:30:46.578+01:00 ERROR 72640 --- [           main] c.c.AAD.config.PostgresqlDriver          : ⚠️ Error executing script _procedures.sql: ERROR: cannot change name of input parameter "p_student_id"
Hint: Use DROP FUNCTION count_enrollments(integer) first.
2025-11-30T12:30:46.611+01:00 ERROR 72640 --- [           main] c.c.AAD.config.PostgresqlDriver          : ⚠️ Error executing script _sample_data.sql: ERROR: relation "alumno" does not exist
Position: 13
2025-11-30T12:30:46.686+01:00  INFO 72640 --- [           main] c.c.AAD.config.PostgresqlDriver          : 📄 Executed script: _schema.sql
2025-11-30T12:30:46.687+01:00  INFO 72640 --- [           main] c.c.AAD.config.PostgresqlDriver          : ✅ Database initialized successfully!

2025-11-30T12:30:47.120+01:00  INFO 72640 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2025-11-30T12:30:47.132+01:00  INFO 72640 --- [           main] com.ciglgal1409.AAD.AadApplication       : Started AadApplication in 2.413 seconds (process running for 2.75)

2025-11-30T12:30:47.216+01:00  INFO 72640 --- [           main] c.c.AAD.repository.StudentRepository     : create OK: Student(id=1, nif=66280457F, name=Miriam, email=miriam@g.educaand.et)
2025-11-30T12:30:47.217+01:00  INFO 72640 --- [           main] c.c.A.a.StudentManagementService         : Estudiante creado: Student(id=1, nif=66280457F, name=Miriam, email=miriam@g.educaand.et)

2025-11-30T12:30:47.273+01:00  INFO 72640 --- [           main] c.c.AAD.repository.ModuleRepository      : create OK: Module(id=1, code=0425, name=Programacion, hours=250)
2025-11-30T12:30:47.276+01:00  INFO 72640 --- [           main] c.c.A.a.StudentManagementService         : Módulo creado: Module(id=1, code=0425, name=Programacion, hours=250)

2025-11-30T12:30:47.301+01:00  INFO 72640 --- [           main] com.ciglgal1409.AAD.AadApplication       : 0 módulos matriculados para el alumno Miriam
Enrolling - Student ID: 1, Module ID: 1

2025-11-30T12:30:47.327+01:00  INFO 72640 --- [           main] c.c.AAD.repository.StudentRepository     : findById OK: Student(id=1, nif=66280457F, name=Miriam, email=miriam@g.educaand.et)
Found student: 1 - Miriam

2025-11-30T12:30:47.331+01:00  INFO 72640 --- [           main] c.c.AAD.repository.ModuleRepository      : findById OK: Module(id=1, code=0425, name=Programacion, hours=250)
Found module: 1 - Programacion

2025-11-30T12:30:47.336+01:00  INFO 72640 --- [           main] c.c.A.repository.EnrollementRepository   : create OK: Enrollment(studentId=1, moduleId=1, date=2025-11-30)
Created enrollment: 1 -> 1

2025-11-30T12:30:47.367+01:00  INFO 72640 --- [           main] c.c.AAD.repository.StudentRepository     : delete OK: true
```