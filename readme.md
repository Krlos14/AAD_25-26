
## 5. Actualización del README.md

### 🔌 ¿Qué es un conector y cuál es su papel en la aplicación?
Un **conector** (driver JDBC) es una librería que permite que la aplicación se comunique con la base de datos.  
Gracias a él, la aplicación puede enviar consultas SQL, recibir resultados y manipular información almacenada en PostgreSQL.  
Sin este driver, la aplicación no podría conectarse a la base de datos.

---

### 🐳 ¿Cómo he levantado el servicio PostgreSQL?
El servicio se ha iniciado utilizando el archivo `docker-compose.yml`, que contiene la configuración del contenedor PostgreSQL.

Pasos realizados:

1. Ejecución del comando:
   ```bash
   docker compose up -d