# Centro Odontológico

Este proyecto es un sistema de gestión para un centro odontológico desarrollado en Java con NetBeans, utilizando patrón MVC y conexión a base de datos MySQL.

## Instrucciones para ejecutar el proyecto

### 1. Descargar el repositorio

Clonar o descargar este repositorio desde:

https://github.com/valencesca11/centro-odontologico

---

### 2. Descargar la base de datos

1. Dentro del repositorio, ir a la carpeta `database/`
2. Descargar el archivo: `Dump20250608.sql`

---

### 3. Importar la base de datos en MySQL Workbench

1. Abrir **MySQL Workbench**.
2. Iniciar sesión en el servidor con los siguientes datos:

   - **Host:** `localhost`
   - **Puerto:** `3306`
   - **Usuario:** `root`
   - **Contraseña:** `1234`

3. Ir a `File > Open SQL Script...` y seleccionar el archivo `Dump20250608.sql` descargado.
4. Ejecutar el script completo con el botón **Run** (⚡) o presionar `Ctrl + Shift + Enter`.

Esto creará todas las tablas necesarias y poblará la base de datos con la información requerida.

---

### 4. Abrir el proyecto en NetBeans

1. Abrir **NetBeans**.
2. Ir a `File > Open Project`.
3. Seleccionar la carpeta del repositorio descargado.
4. Una vez abierto, hacer clic en el botón verde **Run Project** (o presionar `F6`) para ejecutar la aplicación.

---

### 5. Listo

El sistema debería ejecutarse correctamente si los pasos fueron seguidos. Asegurate de tener corriendo tu servidor MySQL antes de ejecutar la aplicación.

### SCOPE DEL PROYECTO

Solamente está desarrollado los botones **Especialidad, Odontologos y Pacientes**.