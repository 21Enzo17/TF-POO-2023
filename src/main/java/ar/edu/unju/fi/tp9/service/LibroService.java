package ar.edu.unju.fi.tp9.service;

import ar.edu.unju.fi.tp9.dto.LibroDeleteDto;
import ar.edu.unju.fi.tp9.dto.LibroEditDto;
import ar.edu.unju.fi.tp9.dto.LibroSaveDto;
import ar.edu.unju.fi.tp9.dto.LibroSearchDto;
import ar.edu.unju.fi.tp9.entity.Libro;
import ar.edu.unju.fi.tp9.exception.ManagerException;

public interface LibroService {
	void guardarLibro(LibroSaveDto libroDto) throws ManagerException;
	
	void eliminarLibro(LibroDeleteDto libroDto);
	
	void editarLibro(LibroEditDto libroDto);
	
	LibroSearchDto buscarLibroPorId(Long id);
	
	LibroSearchDto buscarLibroPorTitulo(String titulo);
	
	LibroSearchDto buscarLibroPorAutor(String autor);
	
	LibroSearchDto buscarLibroPorIsbn(String isbn);
	
	long librosSize();
}
