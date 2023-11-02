package ar.edu.unju.fi.tp9.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.edu.unju.fi.tp9.dto.LibroEliminarDto;
import ar.edu.unju.fi.tp9.dto.LibroEditarDto;
import ar.edu.unju.fi.tp9.dto.LibroGuardarDto;
import ar.edu.unju.fi.tp9.dto.LibroBuscarDto;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.service.LibroService;
import ar.edu.unju.fi.tp9.util.EstadoLibro;

@SpringBootTest
public class LibroServiceTest {
	
	@Autowired
	LibroService libroService;
	
	ModelMapper mapper = new ModelMapper();
	
	LibroGuardarDto libroGuardarDto;
	LibroGuardarDto libroThrowDto1;
	LibroGuardarDto libroThrowDto2;
	LibroEliminarDto libroEliminarDto;
	LibroBuscarDto libroGuardadoDto;
	LibroEditarDto libroEditarDto;
	
	@BeforeEach
	void iniciarVariables() {
		libroGuardarDto = new LibroGuardarDto();
		libroGuardarDto.setTitulo("Un libro");
		libroGuardarDto.setAutor("autor");
		libroGuardarDto.setIsbn("ISBN-10-1234567890");
		libroGuardarDto.setNumeroInventario(111l);
		
		libroThrowDto1 = new LibroGuardarDto();
		libroThrowDto1.setTitulo("Otro libro");
		libroThrowDto1.setAutor("autor");
		libroThrowDto1.setIsbn("ISBN-10-1234567890");
		libroThrowDto1.setNumeroInventario(222l);
		
		libroThrowDto2 = new LibroGuardarDto();
		libroThrowDto2.setTitulo("Otro libro");
		libroThrowDto2.setAutor("autor");
		libroThrowDto2.setIsbn("ISBN-10-0987654321");
		libroThrowDto2.setNumeroInventario(111l);
		
		libroEliminarDto = new LibroEliminarDto();
		libroGuardadoDto = new LibroBuscarDto();
		libroEditarDto = new LibroEditarDto();
	}
	
	@AfterEach
	void resetearVariables() {
		libroGuardarDto = null;
		libroThrowDto1 = null;
		libroThrowDto2 = null;
		libroGuardadoDto = null;
		libroEditarDto = null;
		libroEliminarDto = null;
	}
	
	@Test
	@DisplayName("Test guardar libro")
	void guardarLibroTest() throws ManagerException {
		//Guarda el libro y verifica la cantidad sea correcta.
		libroService.guardarLibro(libroGuardarDto);
		
		assertEquals(6, libroService.librosSize());
		
		//Lo busca y verifica que no sea null, y el isbn sea correcto.
		LibroBuscarDto libroGuardadoDto = libroService.buscarLibroPorTitulo("Un libro");
		assertNotNull(libroGuardadoDto);
		assertEquals(libroGuardadoDto.getIsbn(), "ISBN-10-1234567890");
		
		//Verificación de throw con el isbn repetido.
		assertThrows(ManagerException.class,()->libroService.guardarLibro(libroThrowDto1));
		
		//Verificaicon de throw con numero de inventario repetido.
		assertThrows(ManagerException.class,()->libroService.guardarLibro(libroThrowDto2));
		
		//Elimina el libro de prueba.
		mapper.map(libroGuardadoDto, libroEliminarDto);
		libroService.eliminarLibro(libroEliminarDto);
	}
	
	@Test
	@DisplayName("Test eliminar libro")
	void eliminarLibroTest() throws ManagerException {
		//Guarda un libro.
		libroService.guardarLibro(libroGuardarDto);
		
		//Lo busca y lo mapea a un libroDeleteDto.
		libroGuardadoDto = libroService.buscarLibroPorTitulo("Un libro");
		mapper.map(libroGuardadoDto, libroEliminarDto);
		
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
		libroGuardadoDto = libroService.buscarLibroPorTitulo("IT");
		
		//Lo mapea a un libroEditarDto, lo modifica y manda a editar.
		mapper.map(libroGuardadoDto, libroEditarDto);
		libroEditarDto.setTitulo("Moby dick");
		libroEditarDto.setEstado(EstadoLibro.PRESTADO);
		libroService.editarLibro(libroEditarDto);
		
		//Busca el libro por autor no modificado y verifica que los valores se hayan modificado y que no sea null.
		libroGuardadoDto = libroService.buscarLibroPorAutor("Stephen King");
		assertNotNull(libroGuardadoDto);
		assertEquals(libroGuardadoDto.getTitulo(), "Moby dick");
		assertEquals(libroGuardadoDto.getEstado(), EstadoLibro.PRESTADO);
	}
	
	@Test
	@DisplayName("Test Buscar Libro por Autor")
	void buscarLibroAutorTest() {
		libroGuardadoDto = libroService.buscarLibroPorAutor("J.K Rowling");
		assertNotNull(libroGuardadoDto);
		
		assertEquals(libroGuardadoDto.getTitulo(), "Harry Potter y la piedra filosofal");
		assertEquals(libroGuardadoDto.getEstado(), EstadoLibro.DISPONIBLE);
	}
	
	@Test
	@DisplayName("Test Buscar Libro por Titulo")
	void buscarLibroTitutloTest() {
		libroGuardadoDto = libroService.buscarLibroPorTitulo("El camino de los reyes");
		assertNotNull(libroGuardadoDto);
		
		assertEquals(libroGuardadoDto.getAutor(), "Brandon Sanderson");
		assertEquals(libroGuardadoDto.getIsbn(), "ISBN-10-0061120082");
	}
	
	@Test
	@DisplayName("Test Buscar Libro por ISBN")
	void buscarLibroIsbnTest() {
		libroGuardadoDto = libroService.buscarLibroPorIsbn("ISBN-10-0451524935");
		assertNotNull(libroGuardadoDto);
		
		assertEquals(libroGuardadoDto.getAutor(), "J.R.R. Tolkien");
		assertEquals(libroGuardadoDto.getTitulo(), "El senior de los anillos");
	}
}
