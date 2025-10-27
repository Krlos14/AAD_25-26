# AAD – Event Logger CLI (Spring Boot)

Aplicación de consola en Java (Spring Boot) para **registrar eventos**, **filtrarlos por fecha** y **configurar la codificación** del fichero de log.

> Por defecto almacena y lee con codificación **UTF-8**  
> Permite cambiar a **ISO-8859-1** durante la ejecución

---

## Funcionalidad

Al ejecutar la aplicación se muestra un menú interactivo, con las siguientes funciones :


### 1️ Add event
- Solicita **fecha y hora** (`dd-MM-yyyy HH:mm:ss`)
- Solicita mensaje del evento
- Guarda el evento en `app.log` con el formato

---

### 2️ Filter events
Permite buscar eventos filtrando por fecha parcial o completa.
Ejemplos válidos de búsqueda:
- `01-12-2025`
- `01-12-2025 00:14:01`

Las coincidencias se imprimen en consola.

---

### 3️ Configurate codificate
Permite seleccionar la codificación:
- `UTF-8`
- `ISO_8859_1`

Todos los accesos posteriores al fichero usan la configuración seleccionada.

---

### 4 Exit
Sale de la aplicación.

---

## Detalles Técnicos

- **Framework:** Spring Boot (`CommandLineRunner`)
- **Librerías adicionales:** Lombok (`@Slf4j`)
- **Gestión de fechas:** `LocalDateTime` + `DateTimeFormatter`
- **Gestión de ficheros:** `BufferedWriter/BufferedReader` con `Charset`
- **Interfaz:** consola mediante `Scanner`

---

## Formato del fichero `app.log`

Ejemplo de contenido:

- [01-12-2025 00:14:01] Usuario: Inicio de sesión
- [01-12-2025 13:22:45] Usuario: Se añadió un pedido

Cada línea representa un evento independiente.

---

## Ejemplos de ejecución

### Añadir evento
Entrada:
- 01-12-2025 13:22:45
- Se añadió un pedido

Resultado:
- [01-12-2025 13:22:45] Usuario: Se añadió un pedido

---

### Filtrar eventos
Entrada: 01-12-2025

Salida:
- Event found : [01-12-2025 00:14:01] Usuario: Inicio de sesión
-  Event found : [01-12-2025 13:22:45] Usuario: Se añadió un pedido

---

### Cambiar codificación
Configuration default is: UTF-8
Select a new configuration

UTF-8

ISO-8859-1

---

## Requisitos

- Java **17+** (o la versión configurada en tu proyecto)
- Maven o IDE compatible con Spring Boot
- Plugin de Lombok habilitado en el IDE
- Consola interactiva

---

## Instalación y Ejecución

### Recomendado (IDE)
Botón ▶ "Run" desde IntelliJ, VSCode o Eclipse.

### Terminal (Maven)
```bash
mvn spring-boot:run
