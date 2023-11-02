package ar.edu.unju.fi.tp9.service;

import ar.edu.unju.fi.tp9.dto.LibroEliminarDto;
import ar.edu.unju.fi.tp9.dto.LibroEditarDto;
import ar.edu.unju.fi.tp9.dto.LibroGuardarDto;
import ar.edu.unju.fi.tp9.dto.LibroBuscarDto;
import ar.edu.unju.fi.tp9.entity.Libro;
import ar.edu.unju.fi.tp9.exception.ManagerException;

public interface LibroService {
	void guardarLibro(LibroGuardarDto libroDto) throws ManagerException;
	
	void eliminarLibro(LibroEliminarDto libroDto);
	
	void editarLibro(LibroEditarDto libroDto);
	
	LibroBuscarDto buscarLibroPorId(Long id);
	
	LibroBuscarDto buscarLibroPorTitulo(String titulo);
	
	LibroBuscarDto buscarLibroPorAutor(String autor);
	
	LibroBuscarDto buscarLibroPorIsbn(String isbn);
	
	long librosSize();
}
