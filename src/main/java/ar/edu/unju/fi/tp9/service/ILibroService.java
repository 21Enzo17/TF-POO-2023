package ar.edu.unju.fi.tp9.service;

import ar.edu.unju.fi.tp9.dto.LibroDto;
import ar.edu.unju.fi.tp9.entity.Libro;
import ar.edu.unju.fi.tp9.exception.ManagerException;

public interface ILibroService {
	void guardarLibro(LibroDto libroDto) throws ManagerException;
	
	void eliminarLibro(LibroDto libroDto);
	
	void editarLibro(LibroDto libroDto);
	
	LibroDto buscarLibroPorId(Long id);
	
	LibroDto buscarLibroPorTitulo(String titulo);
	
	LibroDto buscarLibroPorAutor(String autor);
	
	LibroDto buscarLibroPorIsbn(String isbn);
	
	long librosSize();

	LibroDto libroALibroDto(Libro libro);

	Libro libroDtoALibro(LibroDto libroDto);
}
