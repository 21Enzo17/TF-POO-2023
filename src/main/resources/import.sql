insert into libros (titulo, autor, isbn, num_inventario, estado) values ("IT", "Stephen King", "ISBN-10-0451450523", 554, "DISPONIBLE");
insert into libros (titulo, autor, isbn, num_inventario, estado) values ("Harry Potter y la piedra filosofal", "J.K Rowling", "ISBN-10-0553213504", 313, "DISPONIBLE");
insert into libros (titulo, autor, isbn, num_inventario, estado) values ("El camino de los reyes", "Brandon Sanderson", "ISBN-10-0061120082", 442, "PRESTADO");
insert into libros (titulo, autor, isbn, num_inventario, estado) values ("El senior de los anillos", "J.R.R. Tolkien", "ISBN-10-0451524935", 973, "PRESTADO");
insert into libros (titulo, autor, isbn, num_inventario, estado) values ("Don Quijote de la Mancha", "Miguel de Cervantes", "ISBN-10-0674395762", 148, "BAJA");

insert into prestamos (id_libro, fecha_prestamo, fecha_devolucion, estado) values (1, "05/06/2023 18:00", "10/06/2023 18:00", "PRESTADO");