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

@Service
public class LibroServiceImp implements ILibroService{

	@Autowired
	LibroRepository libroRepository;
	
	private static Logger logger = Logger.getLogger(LibroServiceImp.class);

	private static ModelMapper mapper = new ModelMapper();
	

	/**
	 * Guarda un libro DTO pasado por parametro, verificando que su ISBN y numero de inventario no hayan sido registrado previamente.
	 */
	@Override
	public void guardarLibro(LibroDto libroDto) throws ManagerException {
		Libro nuevoLibro = new Libro();
		
		verificarIsbnNumeroInventario(libroDto.getIsbn(), libroDto.getNumeroInventario());
				
		mapper.map(libroDto, nuevoLibro);
		nuevoLibro.setEstado(EstadoLibro.DISPONIBLE.toString());
		
		libroRepository.save(nuevoLibro);
		logger.info("Libro registrado con exito, " + nuevoLibro.getId());
	}

	/**
	 * Recibe un id por parametro y verifica que exista previamente antes de eliminarlo.
	 * @throws ManagerException 
	*/
	@Override
	public void eliminarLibro(Long id) throws ManagerException {
		Optional<Libro> libro = libroRepository.findById(id);
		
		if(libro.isEmpty()) {
			logger.error("Libro no registrado, " + id);
			throw new ManagerException("Libro con id: " + id + " no ha sido registrado.");
		}
		else {
			try{
				libroRepository.delete(libro.get());
				logger.info("Libro eliminado correctamente, " + id);
			}catch(Exception e) {
				logger.error("Error al eliminar el libro con id: " + id + ", " + e.getMessage());
				throw new ManagerException("Error al eliminar el libro, " + id);
			}			
		}
	}

	/**
	 * Recibe un libro DTO por parametro, es mapeado un libro y verifica que exista previamente antes de editarlo.
	 * @throws ManagerException 
	 */
	@Override
	public void editarLibro(LibroDto libroDto) throws ManagerException {
		Libro editarLibro = new Libro();
		
		
		if( libroRepository.existsById(libroDto.getId()) ) {
			mapper.map(libroDto, editarLibro);
			libroRepository.save(editarLibro);
			logger.info("Libro modificado con exito, " + libroDto.getId());
		}
		else {
			logger.error("Este libro no existe, " + libroDto.getId());
			throw new ManagerException("Libron con id: " + libroDto.getId() + " no ha sido registrado.");
		}
	}
	
	/**
	 * Busca un libro por id pasado por parametro, si existe lo mapea a libro DTO y lo devuelve, de lo contrario devuelve null.
	 * @throws ManagerException 
	 */
	@Override
	public LibroDto buscarLibroPorId(Long id) throws ManagerException {
		LibroDto libroDto = new LibroDto();
		Optional<Libro> libroBuscado = libroRepository.findById(id);
		
		if(libroBuscado.isEmpty()) {
			logger.error("Este libro no existe, " + id);
			throw new ManagerException("El libro con el id:" + id + " no ha sido registrado");
		}	
		else {
			mapper.map(libroBuscado.get(), libroDto);
			logger.info("Se encontro el libro " + libroDto.getTitulo());
			return libroDto;
		}
	}

	/**
	 * Busca un libro por titulo si lo encuentra devuelve dto, sino, null, 
	 * @throws ManagerException 
	 */
	@Override
	public LibroDto buscarLibroPorTitulo(String titulo) throws ManagerException {
		LibroDto libroDto = new LibroDto();
		Libro libroBuscado = libroRepository.findByTitulo(titulo);
		
		if(libroBuscado == null) {
			logger.error("Libro no encontrado, " + titulo);
			throw new ManagerException("Libro con el siguiente titulo no encontrado: " + titulo);
		}
		else {
			libroDto = libroALibroDto(libroBuscado);
			logger.info("Libro encontrado " + titulo);
			return libroDto;
		}
	}

	/**
	 * Busca un libro por autor pasado por parametro y lo mapea a DTO, si no existe, devuelve null.
	 * @throws ManagerException 
	 */
	@Override
	public List<LibroDto> buscarLibroPorAutor(String autor) throws ManagerException {
		List<Libro> librosBuscados = libroRepository.findAllByAutor(autor);
		if(librosBuscados.isEmpty()) {
			logger.error("No hay libros registrados de " + autor);
			throw new ManagerException("No hay libros registrados de " + autor);
		}
		return devolverListaLibrosDto(librosBuscados);
	}

	/**
	 * Devuelve un libroDto buscado por isbn pasado por parametro, si no existe, sino devuelve null.
	 * @throws ManagerException 
	 */
	@Override
	public List<LibroDto> buscarLibroPorIsbn(String isbn) throws ManagerException {
		List<Libro> librosBuscados = libroRepository.findAllByIsbn(isbn);
		if(librosBuscados.isEmpty()) {
			logger.error("No hay libros registrados con el isbn: " + isbn);
			throw new ManagerException("No hay libros registrados con el isbn: " + isbn);
		}
		return devolverListaLibrosDto(librosBuscados);
	}
	
	@Override
	public long librosSize() {
		logger.info("Devolviendo cantidad de libros registrados.");
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
		return libro;
	}

	/**
	 * Metodo que le cambia el estado a un libro por id
	 * @param id
	 * @param estado
	 * @throws ManagerException
	 */
	@Override
	public void cambiarEstado(Libro libro, String estado) throws ManagerException {
		libro.setEstado(estado);
		libroRepository.save(libro);
		logger.info("Estado cambiado, " + libro.getTitulo());
	}


	/**
	 * Metodo que se encarga de verificar si un libro esta disponible, en caso de no estarlo
	 * lanza una excepcion personalizada
	 * @param id
	 * @throws ManagerException
	 */
	@Override
	public void verificarLibroDisponible(Libro libro) throws ManagerException {
		if( !libro.getEstado().equals(EstadoLibro.DISPONIBLE.toString()) ){
			logger.error("Libro no esta disponible, " + libro.getId());
			throw new ManagerException("El libro " + libro.getTitulo() + " ya ha sido prestado.");
		}
	}

	private void verificarIsbnNumeroInventario(String isbn, Long numeroInventario) throws ManagerException {
		if( libroRepository.findByIsbnAndNumeroInventario(isbn, numeroInventario) != null ) {
			logger.error("Libro con ISBN " + isbn + " y numero de inventario " + numeroInventario + " ya registrado");
			throw new ManagerException("Libro con ISBN " + isbn + " y numero de inventario " + numeroInventario + " ya registrado");
		}
	}

	private List<LibroDto> devolverListaLibrosDto(List<Libro> libros){
		List<LibroDto> librosDto = new ArrayList<>();
		
		if(libros.size() == 0) {
			logger.info("No hay libros devolviendo lista vacia");
			return librosDto;
		}
		else {
			for(Libro libro : libros)
				librosDto.add(libroALibroDto(libro));
			logger.info("Devolviendo lista de libros librosDto");
			return librosDto;
		}
	}
	
}
