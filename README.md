# Prueba Técnica – Sistema de Gestión de Usuarios

Aplicación de consola (CLI) desarrollada en **Java 8**, sin frameworks, que implementa un mantenedor de usuarios utilizando **JDBC puro** y **PostgreSQL**.  
El proyecto utiliza **Maven** para la gestión de dependencias y build.

---

## 📋 Requisitos del Entorno

- Java JDK 8 (1.8.x)
- PostgreSQL 9.6 o superior
- Apache Maven 3.9+
- Windows (scripts `.bat` incluidos)
- IDE recomendado: IntelliJ IDEA

### Verificar instalación

```bash
java -version
psql --version
mvn -version

🗄️ Base de Datos

El proyecto incluye el archivo schema.sql con la definición completa del esquema y datos de prueba.

Crear base de datos y tablas
psql -U postgres -f schema.sql


Este script:

Crea la base de datos prueba_tecnica

Crea la tabla usuarios

Inserta registros de prueba

⚙️ Configuración de conexión

La conexión a PostgreSQL se define mediante constantes en el código:

Host: localhost

Puerto: 5432

Base de datos: prueba_tecnica

Usuario: postgres

Password: postgres (ajustar según instalación local)

La conexión se realiza usando DriverManager y PreparedStatement.

🧱 Arquitectura del Proyecto

Separación clara de capas:

prueba-tecnica/
├── app/
│   ├── Main.java                  # Interfaz CLI
│   ├── model/
│   │   └── Usuario.java           # Modelo de dominio
│   ├── service/
│   │   └── UsuarioService.java    # Lógica de negocio y validaciones
│   └── repository/
│       └── UsuarioRepository.java # Acceso a datos (JDBC)
├── src/
│   └── main/resources/
│       └── application.properties (opcional)
├── src/test/java/
│   └── app/
│       └── UsuarioServiceValidationTest.java
├── schema.sql
├── pom.xml
├── compilar.bat
└── ejecutar.bat

🖥️ Funcionalidades Implementadas (CRUD)

Menú CLI:

1) Listar usuarios
2) Buscar usuario por ID
3) Crear usuario
4) Editar usuario
5) Activar usuario
6) Desactivar usuario
0) Salir

Funcionalidades

Listar usuarios (formato tabular)

Crear usuario

Editar usuario

Activar / desactivar usuario

Búsqueda por ID

🔐 Validaciones y Reglas

Campos obligatorios

Formato básico de email

Email duplicado (controlado por BD)

Uso exclusivo de PreparedStatement

Prevención de SQL Injection

Manejo de errores con try / catch

Cierre correcto de recursos (try-with-resources)

🧪 Tests Unitarios (Bonus)

Se incluyen tests básicos con JUnit 5, enfocados en validaciones de negocio (sin acceso a base de datos).

Ejecutar tests
mvn test


O desde IntelliJ IDEA:

Maven → Lifecycle → test

🚀 Compilación
Opción A – Script Windows
compilar.bat

Opción B – Maven
mvn clean package


El build genera:

target/gestion-usuarios-1.0.0-jar-with-dependencies.jar

▶️ Ejecución
Opción A – Script Windows
ejecutar.bat

Opción B – Manual
java -jar target/gestion-usuarios-1.0.0-jar-with-dependencies.jar

📦 Entregable

Código fuente completo

schema.sql

pom.xml

Scripts .bat

JAR ejecutable con dependencias

Tests unitarios

Este README

✅ Consideraciones Finales

Proyecto desarrollado sin frameworks

Uso correcto de JDBC y SQL

Arquitectura clara y mantenible

Compatible con ejecución por consola y por IDE

Cumple requisitos técnicos y criterios avanzados solicitados