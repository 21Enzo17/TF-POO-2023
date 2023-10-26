package ar.edu.unju.fi.tp9.service.impl;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.entity.Libro;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.repository.LibroRepository;
import ar.edu.unju.fi.tp9.service.LibroService;
import jakarta.persistence.EntityNotFoundException;

@Service
public class LibroServiceImpl implements LibroService{

	@Autowired
	LibroRepository libroRepository;
	
	private static Logger logger = Logger.getLogger(LibroServiceImpl.class);
	
	/**
	 * Verifica que el libro a guardar no tenga un ISBN y numero de inventario ya registrados.
	 * @param libro a verificar.
	 * @return boolean, false si se puede registrar, true si no. 
	 */
	private boolean verificarIsbnNumInventario(Libro libro) {
		Libro libroIsbn = libroRepository.findByIsbn(libro.getIsbn());
		
		if(libroIsbn == null)
			return false; //Si el libro con ISBN pasado no existe devuelve falso
		else if (libroIsbn.getNumeroInventario() != libro.getNumeroInventario())
			return false; //Si el libro con el mismo ISBN tiene un numero de inventario diferente devuelve falso
		else
			return true; //Devuelve verdadero si ISBN y numero de inventario son iguales.
	}
	
	
	/**
	 * Guarda un libro pasado por parametro si el libro no tiene un ISBN y numero de inventario ya registrado.
	 */
	//FIXME Aplicar DTO
	@Override
	public void guardarLibro(Libro libro) throws ManagerException{
		if(verificarIsbnNumInventario(libro)) {
			logger.error("ISBN " + libro.getIsbn() + "registrado con el mismo numero de inventario");
			throw new ManagerException("ISBN " + libro.getIsbn() + " registrado con el mismo numero de inventario");
		}
		else{
			logger.info("El libro " + libro.getTitulo() + " de " + libro.getAutor() + " registrado exitosamente.");
			libroRepository.save(libro);
		}
	}

	/**
	 * Elimina un libro por su id, si es que existe un libro registrado con el id pasado por parametro.
	 */
	//FIXME Aplicar DTO
	@Override
	public void eliminarLibro(Long id){
		Optional<Libro> libroEliminar = libroRepository.findById(id);
		
		if(libroEliminar.isEmpty()) {
			logger.error("La id: " + id + " no este registrada para ningun libro.");
			throw new EntityNotFoundException("La id: " + id + " no este registrada para ningun libro.");
		}
		else {
			logger.info("Libro con id: " + id + "eliminado con exito.");
			libroRepository.delete(libroEliminar.get());
		}
	}

	/**
	 * Edita un libro pasado por parametro si es que su id fue registrado previamente.
	 */
	//FIXME Aplicar DTO
	@Override
	public void editarLibro(Libro libro){
		if( !libroRepository.existsById(libro.getId()) ) {
			logger.error("El libro " + libro.getTitulo() + " no se encuentra registrado.");
			throw new EntityNotFoundException("El libro con id: " + libro.getId() + " no se encuentra registrado.");
		}
		else {
			logger.info("El libro con id: " + libro.getId() + " se modifico correctamente.");
			libroRepository.save(libro);
		}
	}

	/**
	 * Busca un libro por id, si no lo encuentra devuelve null.
	 */
	@Override
	public Libro buscarLibroPorId(Long id) {
		Optional<Libro> libroId = libroRepository.findById(id);
		if(libroId.isEmpty())
			return null;
		else
			return libroId.get();
	}

	/**
	 * Busca un libro por titulo, si no lo encuentra devuelve null.
	 */
	@Override
	public Libro buscarLibroPorTitulo(String titulo) {
		return libroRepository.findByTitulo(titulo);
	}

	/**
	 * Busca un libro por autor, si no lo encuentra devuelve null.
	 */
	@Override
	public Libro buscarLibroPorAutor(String autor) {
		return libroRepository.findByAutor(autor);
	}

	/**
	 * Busca un libro por ISBN, si no lo encuentra devuelve null.
	 */
	@Override
	public Libro buscarLibroPorIsbn(String isbn) {
		return libroRepository.findByIsbn(isbn);
	}
}
