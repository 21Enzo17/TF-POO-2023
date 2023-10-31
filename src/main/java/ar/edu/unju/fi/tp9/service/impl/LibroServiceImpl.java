package ar.edu.unju.fi.tp9.service.impl;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.dto.LibroDeleteDto;
import ar.edu.unju.fi.tp9.dto.LibroEditDto;
import ar.edu.unju.fi.tp9.dto.LibroSaveDto;
import ar.edu.unju.fi.tp9.dto.LibroSearchDto;
import ar.edu.unju.fi.tp9.entity.Libro;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.repository.LibroRepository;
import ar.edu.unju.fi.tp9.service.LibroService;
import ar.edu.unju.fi.tp9.util.EstadoLibro;
import jakarta.persistence.EntityNotFoundException;

@Service
public class LibroServiceImpl implements LibroService{

	@Autowired
	LibroRepository libroRepository;
	
	private static Logger logger = Logger.getLogger(LibroServiceImpl.class);

	private static ModelMapper mapper = new ModelMapper();
	
	/**
	 * Guarda un libro DTO pasado por parametro, verificando que su ISBN y numero de inventario no hayan sido registrado previamente.
	 */
	@Override
	public void guardarLibro(LibroSaveDto libroDto) throws ManagerException {
		Libro nuevoLibro = new Libro();
		mapper.map(libroDto, nuevoLibro);

		if(libroRepository.existsByIsbn(nuevoLibro.getIsbn()) ) {
			logger.error("ISBN: " + nuevoLibro.getIsbn() + " ya ah sido registrado.");
			throw new ManagerException("ISBN: " + nuevoLibro.getIsbn() + " ya ah sido registrado.");
		}
		else if(libroRepository.existsByNumeroInventario(nuevoLibro.getNumeroInventario())) {
			logger.error("Numero de inventario: " + nuevoLibro.getNumeroInventario() + " ya ah sido registrado.");
			throw new ManagerException("Numero de inventario: " + nuevoLibro.getNumeroInventario() + " ya ah sido registrado.");
		}
		else {
			nuevoLibro.setEstado(EstadoLibro.DISPONIBLE);
			libroRepository.save(nuevoLibro);
			logger.info("El libro " + nuevoLibro.getTitulo() + " se registro con exito");
		}
	}

	/**
	 * Recibe un libro DTO por parametro, mapeo un libro y verifica que exista previamente antes de eliminarlo.
	 */
	@Override
	public void eliminarLibro(LibroDeleteDto libroDto) {
		Libro eliminarLibro = new Libro();
		mapper.map(libroDto, eliminarLibro);
		
		if(libroRepository.existsById(eliminarLibro.getId())) {
			libroRepository.delete(eliminarLibro);
			logger.info("El libro "+ eliminarLibro.getTitulo() + " ah sido eliminado.");
		}
		else {
			logger.error("Libro con id: " + eliminarLibro.getId() + " no ah sido registrado.");
			throw new EntityNotFoundException("Libro con id: " + eliminarLibro.getId() + " no ah sido registrado.");
		}
	}

	/**
	 * Recibe un libro DTO por parametro, es mapeado un libro y verifica que exista previamente antes de editarlo.
	 */
	@Override
	public void editarLibro(LibroEditDto libroDto) {
		Libro editarLibro = new Libro();
		mapper.map(libroDto, editarLibro);
		
		if(libroRepository.existsById(editarLibro.getId())) {
			libroRepository.save(editarLibro);
			logger.info("Libro con id: " + editarLibro.getId() + " ah sido modificado.");
		}
		else {
			logger.error("Libron con id: " + editarLibro.getId() + " no ah sido registrado.");
			throw new EntityNotFoundException("Libron con id: " + editarLibro.getId() + " no ah sido registrado.");
		}
	}

	/**
	 * Busca un libro por id pasado por parametro, si existe lo mapea a libro DTO y lo devuelve, de lo contrario devuelve null.
	 */
	@Override
	public LibroSearchDto buscarLibroPorId(Long id) {
		
		LibroSearchDto libroDto = new LibroSearchDto();
		Optional<Libro> libroBuscado = libroRepository.findById(id);
		
		if(libroBuscado.isEmpty())
			return null;
		else {
			mapper.map(libroBuscado.get(), libroDto);
			logger.info("Devolviendo el libro " + libroDto.getTitulo());
			return libroDto;
		}
	}

	/**
	 * Busca un libro por titulo, 
	 */
	@Override
	public LibroSearchDto buscarLibroPorTitulo(String titulo) {
		LibroSearchDto libroDto = new LibroSearchDto();
		Libro libroBuscado = libroRepository.findByTitulo(titulo);
		
		if(libroBuscado == null)
			return null;
		else {
			mapper.map(libroBuscado, libroDto);
			return libroDto;
		}
	}

	/**
	 * Busca un libro por autor pasado por parametro y lo mapea a DTO, si no existe, devuelve null.
	 */
	@Override
	public LibroSearchDto buscarLibroPorAutor(String autor) {
		LibroSearchDto libroDto = new LibroSearchDto();
		Libro libroBuscado = libroRepository.findByAutor(autor);
		
		if(libroBuscado == null)
			return null;
		else {
			mapper.map(libroBuscado, libroDto);
			return libroDto;
		}
	}

	/**
	 * Busca un 
	 */
	@Override
	public LibroSearchDto buscarLibroPorIsbn(String isbn) {
		LibroSearchDto libroDto = new LibroSearchDto();
		Libro libroBuscado = libroRepository.findByIsbn(isbn);
		
		if(libroBuscado == null)
			return null;
		else {
			mapper.map(libroBuscado, libroDto);
			return libroDto;
		}
	}

	@Override
	public long librosSize() {
		return libroRepository.count();
	}
}
