package ar.edu.unju.fi.tp9.service.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.dto.LibroDto;
import ar.edu.unju.fi.tp9.entity.Libro;
import ar.edu.unju.fi.tp9.enums.EstadoLibro;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.repository.LibroRepository;
import ar.edu.unju.fi.tp9.service.ILibroService;
import jakarta.persistence.EntityNotFoundException;

@Service
public class LibroServiceImp implements ILibroService{

	@Autowired
	LibroRepository libroRepository;
	
	private static Logger logger = Logger.getLogger(LibroServiceImp.class);

	private static ModelMapper mapper = new ModelMapper();
	
	
	private boolean estaIsbnRegistrado(String isbn) {
		return libroRepository.existsByIsbn(isbn);
	}
	
	/**
	 * Guarda un libro DTO pasado por parametro, verificando que su ISBN y numero de inventario no hayan sido registrado previamente.
	 */
	@Override
	public void guardarLibro(LibroDto libroDto) throws ManagerException {
		Libro nuevoLibro = new Libro();
		
		if(estaIsbnRegistrado(libroDto.getIsbn()))
			if(buscarLibroPorIsbn(libroDto.getIsbn()).getNumeroInventario() == libroDto.getNumeroInventario()) {
				logger.error("Libro con ISBN " + libroDto.getIsbn() + " y numero de inventario " + libroDto.getNumeroInventario() + " ya registrado");
				throw new ManagerException("Libro con ISBN " + libroDto.getIsbn() + " y numero de inventario " + libroDto.getNumeroInventario() + " ya registrado");
			}
				
			mapper.map(libroDto, nuevoLibro);
			nuevoLibro.setEstado(EstadoLibro.DISPONIBLE.toString());
			libroRepository.save(nuevoLibro);
			logger.debug("El libro " + nuevoLibro.getTitulo() + " se registro con exito");
	}

	/**
	 * Recibe un id por parametro y verifica que exista previamente antes de eliminarlo.
	 */
	@Override
	public void eliminarLibro(Long id) {
		Optional<Libro> libro = libroRepository.findById(id);
		
		if(libro.isEmpty()) {
			logger.error("Libro con id: " + id + " no ha sido registrado.");
			throw new EntityNotFoundException("Libro con id: " + id + " no ha sido registrado.");
		}
		else {
			libroRepository.delete(libro.get());
			logger.debug("El libro con id: "+ id + " ha sido eliminado.");
		}
	}

	/**
	 * Recibe un libro DTO por parametro, es mapeado un libro y verifica que exista previamente antes de editarlo.
	 */
	@Override
	public void editarLibro(LibroDto libroDto) {
		Libro editarLibro = new Libro();
		
		
		if( libroRepository.existsById(libroDto.getId()) ) {
			mapper.map(libroDto, editarLibro);
			libroRepository.save(editarLibro);
			logger.debug("Libro con id: " + editarLibro.getId() + " ha sido modificado.");
		}
		else {
			logger.error("Libron con id: " + libroDto.getId() + " no ha sido registrado.");
			throw new EntityNotFoundException("Libron con id: " + libroDto.getId() + " no ha sido registrado.");
		}
	}

	/**
	 * Busca un libro por id pasado por parametro, si existe lo mapea a libro DTO y lo devuelve, de lo contrario devuelve null.
	 */
	@Override
	public LibroDto buscarLibroPorId(Long id) {
		
		LibroDto libroDto = new LibroDto();
		Optional<Libro> libroBuscado = libroRepository.findById(id);
		
		if(libroBuscado.isEmpty())
			return null;
		else {
			mapper.map(libroBuscado.get(), libroDto);
			logger.debug("Libro Encontrado " + libroDto.getTitulo());
			return libroDto;
		}
	}

	/**
	 * Busca un libro por titulo si lo encuentra devuelve dto, sino, null, 
	 */
	@Override
	public LibroDto buscarLibroPorTitulo(String titulo) {
		LibroDto libroDto = new LibroDto();
		Libro libroBuscado = libroRepository.findByTitulo(titulo);
		
		if(libroBuscado == null) {
			logger.info("Libro buscado por titulo no encontrado, devolviendo null");
			return null;
		}
		else {
			mapper.map(libroBuscado, libroDto);
			logger.info("Devolviendo libro buscado por titulo");
			return libroDto;
		}
	}

	/**
	 * Busca un libro por autor pasado por parametro y lo mapea a DTO, si no existe, devuelve null.
	 */
	@Override
	public List<LibroDto> buscarLibroPorAutor(String autor) {
		List<LibroDto> librosDto = new ArrayList<>();
		List<Libro> libroBuscado = libroRepository.findAllByAutor(autor);
		
		if(libroBuscado.size() == 0) {
			logger.info("No hay libros de " + autor + " devolviendo lista vacia");
			return librosDto;
		}
		else {
			for(Libro p : libroBuscado)
				librosDto.add(libroALibroDto(p));
			logger.info("Devolviendo los librosDto del autor " + autor);
			return librosDto;
		}
	}

	/**
	 * Devuelve un libroDto buscado por isbn pasado por parametro, si no existe, sino devuelve null.
	 */
	@Override
	public LibroDto buscarLibroPorIsbn(String isbn) {
		LibroDto libroDto = new LibroDto();
		Libro libroBuscado = libroRepository.findByIsbn(isbn);
		
		if(libroBuscado == null) {
			logger.info("Libro por isbn no encontrado, devolviendo null");
			return null;
		}
		else {
			mapper.map(libroBuscado, libroDto);
			logger.info("Devolviendo libro buscado por ISBN");
			return libroDto;
		}
	}

	
	@Override
	public long librosSize() {
		logger.info("devolviendo cantidad de libros registrados.");
		return libroRepository.count();
	}
	
	/**
	 * Metodo que transforma un libro a libroDto
	 * @param libro
	 * @return libroDto
	 */
	@Override
	public LibroDto libroALibroDto(Libro libro) {
		LibroDto libroDto = new LibroDto();
        mapper.map(libro, libroDto);
        logger.info("Devolviendo libroDto mapeado.");
		return libroDto;
	}


	/**
	 * Metodo que transforma un libroDto a libro
	 * @param libroDto
	 * @return libro
	 */
	@Override
	public Libro libroDtoALibro(LibroDto libroDto) {
		Libro libro = new Libro();
		mapper.map(libroDto, libro);
		logger.info("Devolviendo libro mapeado.");
		return libro;
	}

	@Override
	public void cambiarEstado(Long id, String estado) throws ManagerException {
		Libro libro = libroRepository.findById(id).orElse(null);
		if(libro != null) {
			libro.setEstado(estado);
			libroRepository.save(libro);
			logger.info("Se cambio el estado a libro.");
		}else{
			logger.error("No existe el libro con id: " + id);
			throw new ManagerException("No existe el libro con id: " + id);
		}
	}

	@Override
	public void verificarLibroDisponible(Long id) throws ManagerException {
		LibroDto libroBuscado = buscarLibroPorId(id);
		if(libroBuscado == null) {
			logger.error("Libro no registrado");
			throw new ManagerException("Libro no registrado");
		}
		else {
			if( !libroBuscado.getEstado().equals(EstadoLibro.DISPONIBLE.toString()) ){
				logger.error("El libro no esta disponible");
				throw new ManagerException("El libro " + libroBuscado.getTitulo() + " ya ha sido prestado.");
			}
		}
	}
}
