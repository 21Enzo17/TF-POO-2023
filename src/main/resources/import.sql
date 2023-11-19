insert into libros (titulo, autor, isbn, num_inventario, estado) values ("IT", "Stephen King", "ISBN-10-0451450523", 554, "DISPONIBLE");
insert into libros (titulo, autor, isbn, num_inventario, estado) values ("Harry Potter y la piedra filosofal", "J.K Rowling", "ISBN-10-0553213504", 313, "DISPONIBLE");
insert into libros (titulo, autor, isbn, num_inventario, estado) values ("El camino de los reyes", "Brandon Sanderson", "ISBN-10-0061120082", 442, "PRESTADO");
insert into libros (titulo, autor, isbn, num_inventario, estado) values ("El senior de los anillos", "J.R.R. Tolkien", "ISBN-10-0451524935", 973, "PRESTADO");
insert into libros (titulo, autor, isbn, num_inventario, estado) values ("Don Quijote de la Mancha", "Miguel de Cervantes", "ISBN-10-0674395762", 148, "BAJA");

insert into miembros (numero_miembro, tipo, correo, nombre, numero_telefonico, legajo, fecha_bloqueo) values (15, 'docente', 'enzo.meneghini@hotmail.com', 'Juan', '3885485126', 'FI0025', '2023-11-11 12:00');
insert into miembros (numero_miembro, tipo, correo, nombre, numero_telefonico, libreta_universitaria, fecha_bloqueo) values (16, "alumno", "mauri6030@gmail.com","Roberto","381616064", "APU4073", '2023-12-24 12:00');


insert into prestamos(id_miembro, id_libro, fecha_prestamo, fecha_devolucion, estado) values (1, 3, '2023-11-16 12:00', '2023-11-23 12:00', 0);
insert into prestamos(id_miembro, id_libro, fecha_prestamo, fecha_devolucion, estado) values (2, 4, '2021-05-01 12:00', '2021-05-15 12:00', 0);