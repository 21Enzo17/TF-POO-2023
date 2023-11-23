package ar.edu.unju.fi.tp9.service;

import java.util.List;

import ar.edu.unju.fi.tp9.dto.LibroDto;
import ar.edu.unju.fi.tp9.entity.Libro;
import ar.edu.unju.fi.tp9.exception.ManagerException;

public interface ILibroService {
	void guardarLibro(LibroDto libroDto) throws ManagerException;
	
	void eliminarLibro(Long id);
	
	void editarLibro(LibroDto libroDto);
	
	LibroDto buscarLibroPorId(Long id) throws ManagerException;
	
	LibroDto buscarLibroPorTitulo(String titulo);
	
	List<LibroDto> buscarLibroPorAutor(String autor);
	
	List<LibroDto> buscarLibroPorIsbn(String isbn);
	
	long librosSize();

	LibroDto libroALibroDto(Libro libro);

	Libro libroDtoALibro(LibroDto libroDto);

	void cambiarEstado(Long id, String estado) throws ManagerException;
	
	void verificarLibroDisponible(Long id) throws ManagerException;
}
