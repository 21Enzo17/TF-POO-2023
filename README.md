# TP9-POO
## Profesores
- José Zapana
- Juan Carlos Rodriguez
## Info del proyecto
### Verion de Java
Java 17
### Librerias
* Mysql-Connector-Java
* Spring-Boot-Starter-Test
* Log4j
* Junit
### Integrantes
- Enzo Nicolás Meneghini
- Mauricio Miranda



## Contexto del Problema

Desarrolla una aplicación de gestión de biblioteca utilizando Java y JPA (Java Persistence API). El
sistema deberá permitir a los bibliotecarios llevar un registro de los libros en la biblioteca, los
préstamos realizados a los miembros y la información básica de los miembros.

## Especificación de requerimientos

- **Gestión de Libros**:

  Crear, editar y eliminar registros de libros.
Cada libro debe tener: id, título, autor, ISBN, ~~cantidad disponible~~, número de inventario y estado.
Validaciones: Considere que NO puede existir en la biblioteca, un libro que tenga el mismo ISBN y mismo
número de inventario, implemente la validación correspondiente.

- **Gestión de Miembros**:

  Crear, editar y eliminar registros de miembros de la biblioteca.
Cada miembro debe tener id, nombre, número de miembro, correo electrónico y número de teléfono.
Los miembros están clasificados en alumnos y docentes, Los docentes tienen número de legajo mientras que
los alumnos tienen libreta universitaria, el resto de los atributos es igual para ambos.

  Validaciones: Considere que el email debe ser único para cada miembro, implemente la validación
correspondiente

- **Gestión de préstamos:**
Para cada préstamo se debe registrar: id, miembro, libro, fecha y hora del préstamo, fecha y hora de la
devolución, estado del préstamo (prestado o devuelto).

- **Búsqueda y Consultas**:

  Permitir la búsqueda de libros por título, autor o ISBN.

- **Validación de las funcionalidades**

  Todas las funcionalidades solicitadas deben realizarse utilizando pruebas unitarias

## Consideraciones
- Implemente correctamente el proyecto utilizando Gitlab y agregue al profesor de práctica como miembro del
equipo. Deben crear un nuevo proyecto gitlab con la nueva distribución de grupos de alumnos

- Implemente los patrones de diseño Service Layer, DTO, DI

## Diagrama UML
