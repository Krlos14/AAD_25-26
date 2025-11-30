
## Actividad 2_2 – Migración del acceso a datos a Spring JdbcTemplate

### Descripción General
En esta actividad se ha migrado el sistema de gestión
académica desarrollado en la Actividad 2_1, 
reemplazando el acceso manual a la base de datos mediante 
JDBC tradicional por un enfoque moderno basado en Spring JDBC.

El nuevo sistema utiliza:

- DataSource autoconfigurado por Spring Boot
- JdbcTemplate para operaciones CRUD
- SimpleJdbcCall para funciones almacenadas
- @Transactional para la gestión automática de transacciones
- PostgreSQL alojado en DockerDataSource autoconfigurado por Spring Boo
- JdbcTemplate para operaciones CRUD
- SimpleJdbcCall para funciones almacenadas
- @Transactional para la gestión automática de transacciones
- PostgreSQL alojado en Docker

---

### 2. Configuración del proyecto
Dependencias esencciales (pom.xml)
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```
Estas dependencias permiten:

- Acceso a la BD con DataSource
- Uso de JdbcTemplate, RowMapper, SimpleJdbcCall
- Soporte de PostgreSQL

### 3. Eliminación de JDBC manual
En la actividad se 
- Se elimina por completo la clase PostgresqlDriver
  - Elimina cualquier uso : 
    ```
    Connection
    DriverManager
    PreparedStatement
    CallableStatement
    commit() o rollback() manuales
    ```
  Y todo a sido remplazado por 
  ```
    JdbcTemplate
    SimpleJdbcCall
    @Transactional
    ```
### 4. Migracion de los repositorios a JDBCTemplate
Ejemplo 
 ```
  @Override
    public List<Student> findAll() {
        List<Student> stu = jdbcTemplate.query(SQL_SELECT, (rs, rowNum) -> new Student(
                        rs.getInt("id_alumno"),
                        rs.getString("nif"),
                        rs.getString("nombre"),
                        rs.getString("email")
                )
        );
        log.info("Todos los estudiantes OK");
        return stu;
    }
   ```
### 5.Transaciones con @Trasactional 
Se elimina el uso de transaction.begin(), commit() , rollback().
Ejemplo :
```
    @Transactional
    @Override
    public Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) {
       var student = studentRepository.findById(studentId);
       if (student == null) throw new IllegalArgumentException("Student not found: " + studentId);

       var module = moduleRepository.findById(moduleId);
       if (module == null) throw new IllegalArgumentException("Module not found: " + moduleId);

       return enrollementRepository.insert(
               new Enrollment(student.getId(), module.getId(), LocalDate.now())
       );
    }
````

### 6.Evidencias de ejecución
````    
:: Spring Boot ::                (v3.5.6)

2025-11-30T15:07:58.495+01:00  INFO 79952 --- [           main] com.ciglgal1409.AAD.AadApplication       : Starting AadApplication using Java 21.0.5 with PID 79952 (C:\Users\Gao\IdeaProjects\AAD_25-26\target\classes started by Gao in C:\Users\Gao\IdeaProjects\AAD_25-26)
2025-11-30T15:07:58.499+01:00  INFO 79952 --- [           main] com.ciglgal1409.AAD.AadApplication       : No active profile set, falling back to 1 default profile: "default"
2025-11-30T15:07:59.878+01:00  INFO 79952 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2025-11-30T15:07:59.893+01:00  INFO 79952 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2025-11-30T15:07:59.893+01:00  INFO 79952 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.46]
2025-11-30T15:07:59.939+01:00  INFO 79952 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2025-11-30T15:07:59.939+01:00  INFO 79952 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1378 ms
2025-11-30T15:08:00.951+01:00  INFO 79952 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2025-11-30T15:08:00.959+01:00  INFO 79952 --- [           main] com.ciglgal1409.AAD.AadApplication       : Started AadApplication in 3.385 seconds (process running for 3.848)
2025-11-30T15:08:00.969+01:00  INFO 79952 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2025-11-30T15:08:01.136+01:00  INFO 79952 --- [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection@d325518
2025-11-30T15:08:01.138+01:00  INFO 79952 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2025-11-30T15:08:01.159+01:00  INFO 79952 --- [           main] c.c.AAD.repository.StudentRepository     : Todos los estudiantes OK
2025-11-30T15:08:01.178+01:00  INFO 79952 --- [           main] c.c.AAD.repository.StudentRepository     : create OK: Student(id=3, nif=66280457F, name=Miriam, email=miriam@g.educaand.et)
2025-11-30T15:08:01.182+01:00  INFO 79952 --- [           main] c.c.A.a.StudentManagementService         : Estudiante creado: Student(id=3, nif=66280457F, name=Miriam, email=miriam@g.educaand.et)
2025-11-30T15:08:01.183+01:00  INFO 79952 --- [           main] c.c.AAD.repository.ModuleRepository      : Ejecutando findAll Modules
2025-11-30T15:08:01.189+01:00  INFO 79952 --- [           main] c.c.AAD.repository.ModuleRepository      : FindAll Modules OK
2025-11-30T15:08:01.189+01:00  INFO 79952 --- [           main] c.c.A.a.StudentManagementService         : Módulo ya existente con código 0425. Devolviendo módulo existente.
2025-11-30T15:08:01.191+01:00  INFO 79952 --- [           main] c.c.A.a.StudentManagementService         : Counting enrollments for student ID: 3
2025-11-30T15:08:01.253+01:00  INFO 79952 --- [           main] c.c.A.a.StudentManagementService         : CountEnrollments OK - Student ID: 3 has 0 enrollments
2025-11-30T15:08:01.254+01:00  INFO 79952 --- [           main] com.ciglgal1409.AAD.AadApplication       : 0 módulos matriculados para el alumno Miriam
2025-11-30T15:08:01.278+01:00  INFO 79952 --- [           main] c.c.AAD.repository.StudentRepository     : OK Estudiantes por id=3
2025-11-30T15:08:01.282+01:00  INFO 79952 --- [           main] c.c.AAD.repository.ModuleRepository      : FindById Modules OK id=1
2025-11-30T15:08:01.283+01:00  INFO 79952 --- [           main] c.c.A.repository.EnrollementRepository   : Creating enrollment - Student ID: 3, Module ID: 1, Date: 2025-11-30
2025-11-30T15:08:01.290+01:00  INFO 79952 --- [           main] c.c.A.repository.EnrollementRepository   : Enrollment created successfully
2025-11-30T15:08:01.309+01:00  INFO 79952 --- [           main] c.c.AAD.repository.StudentRepository     : Estudiante eliminado OK id=3
````
### 7.Ventajas de usar JdbcTemplate y DataSource frente a JDBC manual

El uso de JdbcTemplate junto con el DataSource autoconfigurado por Spring Boot aporta múltiples ventajas respecto al acceso manual con Connection,
PreparedStatement, ResultSet y gestión explícita de transacciones:
1. Eliminación de la gestión manual de recursos
   - conexiones
   - sentencias
   - result sets
2. Menos código repetitivo
3. Gestiona las conexiones 
    - Spring utiliza HikariCP, uno de los pools de conexiones más eficientes, 
   sin que el desarrollador tenga que configurarlo manualmente
4. Facilita las pruebas
5. No es neceraio escribir sentencias SQL de forma manual

### 8. Transactional
Transactional permite iniciar una transación al entrar a un método, agrupar operaciones JDBC dentreo de esa misma, hace commits y rollback de forma automatica al salir del metodo
de forma correcta o con error, además de que no necesita de gestión manual.

- Principales ventajas 
  - Evita inconsistencias en operaciones que afectan a varias tablas.
  - Reduce errores humanos al eliminar commit/rollback manuales.
  - Se integra perfectamente con JdbcTemplate y DataSource.
  - Mejora la claridad, robustez y mantenimiento del código.Mejora la claridad, robustez y mantenimiento del código.