package ar.edu.unju.fi.tp9.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unju.fi.tp9.dto.LibroDto;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.service.ILibroService;

@RestController
@RequestMapping("/api/v1/libro")
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class LibroResource {
	
	private static Logger logger = Logger.getLogger(LibroResource.class);
	
	@Autowired
	ILibroService libroService;
	
	@PostMapping("/libros")
	public ResponseEntity<?> guardarLibro(@RequestBody LibroDto libroDto) {
		logger.info("Agregando libro " + libroDto.getTitulo());
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			libroService.guardarLibro(libroDto);
			response.put("Mensaje", "Libro guardado con exito");
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		}catch(ManagerException e) {
			logger.error("Error al guardar el libro: " + e.getMessage());
			response.put("Mensaje", "Error al guardar el libro");
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/libros")
	public ResponseEntity<?> editarLibro(@RequestBody LibroDto libroDto){
		logger.info("Modificando el libro: " + libroDto.getTitulo());
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			libroService.editarLibro(libroDto);
			response.put("Mensaje", "Libro editado correctamente");
			
		}catch (ManagerException e) {
			response.put("Mensaje", "No existe libro guardado con el id enviado");
			response.put("Error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);	
		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/libros/{id}")
	public ResponseEntity<?> eliminarLibro(@PathVariable("id") Long id){
		logger.info("Eliminado libro con id: " + id);
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			libroService.eliminarLibro(id);
			response.put("Mensaje", "Objeto eliminado correctamente");
			
		} catch (ManagerException e) {
			logger.error("No existe libro registrado con id: " + id);
			response.put("Error", e.getMessage());

			if(e.getMessage().contains("no ha sido registrado."))
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/libros/{id}")
	public ResponseEntity<?> buscarLibroPorId(@PathVariable("id") Long id){
		logger.info("Buscando libro por id: " + id);
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			response.put("Objeto", libroService.buscarLibroPorId(id));
			response.put("Mensaje", "libro encontrado con exito");
			
		} catch (ManagerException e) {
			logger.error("No existe libro registrado con id: " + id);
			response.put("Error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/libros/titulo/{titulo}")
	public ResponseEntity<?> buscarLibroPorTitulo(@PathVariable("titulo") String titulo){
		logger.info("Buscando libro por titulo: " + titulo);
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			response.put("Objeto", libroService.buscarLibroPorTitulo(titulo));
			response.put("Mensaje", "libro encontrado con exito");
			
		} catch (ManagerException e) {
			logger.error("Libro no encontrado por titulo " + titulo);
			response.put("Error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/libros/autor/{autor}")
	public ResponseEntity<?> buscarLibroPorAutor(@PathVariable("autor") String autor){
		logger.info("Buscando libro por autor: " + autor);
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			response.put("Objeto", libroService.buscarLibroPorAutor(autor));
			response.put("Mensaje", "libro encontrado con exito");
			
		} catch (ManagerException e) {
			logger.error("Libros no encontrados por el autor " + autor);
			response.put("Error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/libros/isbn/{isbn}")
	public ResponseEntity<?> buscarLibroPorIsbn(@PathVariable("isbn") String isbn){
		logger.info("Buscando libro por ISBN: " + isbn);
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			response.put("Objeto", libroService.buscarLibroPorIsbn(isbn));
			response.put("Mensaje", "libro encontrado con exito");
			
		} catch (ManagerException e) {
			logger.error("Libros no encontrados por isbn " + isbn);
			response.put("Error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
