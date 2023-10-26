package ar.edu.unju.fi.tp9.service;

import ar.edu.unju.fi.tp9.entity.Libro;
import ar.edu.unju.fi.tp9.exception.ManagerException;

public interface LibroService {
	void guardarLibro(Libro libro) throws ManagerException;
	
	void eliminarLibro(Long id);
	
	void editarLibro(Libro libro);
	
	Libro buscarLibroPorId(Long id);
	
	Libro buscarLibroPorTitulo(String titulo);
	
	Libro buscarLibroPorAutor(String autor);
	
	Libro buscarLibroPorIsbn(String isbn);
}
