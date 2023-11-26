package ar.edu.unju.fi.tp9.service;

import java.util.List;

import ar.edu.unju.fi.tp9.dto.LibroDto;
import ar.edu.unju.fi.tp9.entity.Libro;
import ar.edu.unju.fi.tp9.exception.ManagerException;

public interface ILibroService {
	void guardarLibro(LibroDto libroDto) throws ManagerException;
	
	void eliminarLibro(Long id) throws ManagerException;
	
	void editarLibro(LibroDto libroDto) throws ManagerException;
	
	LibroDto buscarLibroPorId(Long id) throws ManagerException;
	
	LibroDto buscarLibroPorTitulo(String titulo) throws ManagerException;
	
	List<LibroDto> buscarLibroPorAutor(String autor) throws ManagerException;
	
	List<LibroDto> buscarLibroPorIsbn(String isbn) throws ManagerException;
	
	long librosSize();

	LibroDto libroALibroDto(Libro libro);

	Libro libroDtoALibro(LibroDto libroDto);

	void cambiarEstado(Libro libro, String estado) throws ManagerException;
	
	void verificarLibroDisponible(Libro libro) throws ManagerException;
}
