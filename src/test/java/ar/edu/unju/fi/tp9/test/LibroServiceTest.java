package ar.edu.unju.fi.tp9.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.edu.unju.fi.tp9.dto.LibroDeleteDto;
import ar.edu.unju.fi.tp9.dto.LibroEditDto;
import ar.edu.unju.fi.tp9.dto.LibroSaveDto;
import ar.edu.unju.fi.tp9.dto.LibroSearchDto;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.service.LibroService;
import ar.edu.unju.fi.tp9.util.EstadoLibro;

@SpringBootTest
public class LibroServiceTest {
	
	@Autowired
	LibroService libroService;
	
	ModelMapper mapper = new ModelMapper();
	
	@Test
	@DisplayName("Test guardar libro")
	void guardarLibroTest() throws ManagerException {
		//Guarda el libro y verifica la cantidad sea correcta.
		LibroSaveDto libroGuardarDto = new LibroSaveDto("Un libro", "autor", "ISBN-10-1234567890", 111l);
		libroService.guardarLibro(libroGuardarDto);
		
		assertEquals(6, libroService.librosSize());
		
		//Lo busca y verifica que no sea null, y el isbn sea correcto.
		LibroSearchDto libroGuardadoDto = libroService.buscarLibroPorTitulo("Un libro");
		assertNotNull(libroGuardadoDto);
		
		assertEquals(libroGuardadoDto.getIsbn(), "ISBN-10-1234567890");
		
		//Verificación de throw con el isbn repetido.
		LibroSaveDto libroThrowDto = new LibroSaveDto("Otro libro", "autor", "ISBN-10-1234567890", 222l);
		assertThrows(ManagerException.class,()->libroService.guardarLibro(libroThrowDto));
		
		//Verificaicon de throw con numero de inventario repetido.
		libroThrowDto.setIsbn("ISBN-10-0987654321");
		libroThrowDto.setNumeroInventario(111l);
		assertThrows(ManagerException.class,()->libroService.guardarLibro(libroThrowDto));
		
		//Elimina el libro de prueba.
		LibroDeleteDto libroBorrarDto = new LibroDeleteDto();
		libroBorrarDto.setId(libroGuardadoDto.getId());
		libroService.eliminarLibro(libroBorrarDto);
	}
	
	@Test
	@DisplayName("Test eliminar libro")
	void eliminarLibroTest() throws ManagerException {
		//Guarda un libro.
		LibroSaveDto libroGuardarDto = new LibroSaveDto("Un libro", "autor", "ISBN-10-1111111111", 777l);
		libroService.guardarLibro(libroGuardarDto);
		
		//Lo busca y lo mapea a un libroDeleteDto.
		LibroSearchDto libroGuardadoDto = libroService.buscarLibroPorTitulo("Un libro");
		LibroDeleteDto libroEliminarDto = new LibroDeleteDto();
		libroEliminarDto.setId(libroGuardadoDto.getId());
		
		//Lo elimina y verifica el total.
		libroService.eliminarLibro(libroEliminarDto);
		assertEquals(5, libroService.librosSize());
		
		//Lo busca y verifica que devuelva null.
		libroGuardadoDto = libroService.buscarLibroPorTitulo("Un libro");
		assertNull(libroGuardadoDto);
	}
	
	@Test
	@DisplayName("Test editar libro")
	void editarLibroTest() {
		//Busca un libro guardado con import.sql
		LibroSearchDto libroBuscarDto = libroService.buscarLibroPorTitulo("IT");
		LibroEditDto libroEditarDto = new LibroEditDto();
		
		//Lo mapea a un libroEditarDto, lo modifica y manda a editar.
		mapper.map(libroBuscarDto, libroEditarDto);
		libroEditarDto.setTitulo("Moby dick");
		libroEditarDto.setEstado(EstadoLibro.PRESTADO);
		libroService.editarLibro(libroEditarDto);
		
		//Busca el libro por autor no modificado y verifica que los valores se hayan modificado y que no sea null.
		libroBuscarDto = libroService.buscarLibroPorAutor("Stephen King");
		assertNotNull(libroBuscarDto);
		assertEquals(libroBuscarDto.getTitulo(), "Moby dick");
		assertEquals(libroBuscarDto.getEstado(), EstadoLibro.PRESTADO);
	}
	
	@Test
	@DisplayName("Test Buscar Libro por Autor")
	void buscarLibroAutorTest() {
		LibroSearchDto libroBuscarDto = libroService.buscarLibroPorAutor("J.K Rowling");
		assertNotNull(libroBuscarDto);
		
		assertEquals(libroBuscarDto.getTitulo(), "Harry Potter y la piedra filosofal");
		assertEquals(libroBuscarDto.getEstado(), EstadoLibro.DISPONIBLE);
	}
	
	@Test
	@DisplayName("Test Buscar Libro por Titulo")
	void buscarLibroTitutloTest() {
		LibroSearchDto libroBuscarDto = libroService.buscarLibroPorTitulo("El camino de los reyes");
		assertNotNull(libroBuscarDto);
		
		assertEquals(libroBuscarDto.getAutor(), "Brandon Sanderson");
		assertEquals(libroBuscarDto.getIsbn(), "ISBN-10-0061120082");
	}
	
	@Test
	@DisplayName("Test Buscar Libro por ISBN")
	void buscarLibroIsbnTest() {
		LibroSearchDto libroBuscarDto = libroService.buscarLibroPorIsbn("ISBN-10-0451524935");
		assertNotNull(libroBuscarDto);
		
		assertEquals(libroBuscarDto.getAutor(), "J.R.R. Tolkien");
		assertEquals(libroBuscarDto.getTitulo(), "El senior de los anillos");
	}
}
