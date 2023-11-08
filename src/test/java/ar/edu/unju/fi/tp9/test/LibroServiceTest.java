package ar.edu.unju.fi.tp9.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.edu.unju.fi.tp9.dto.LibroDto;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.service.ILibroService;
import ar.edu.unju.fi.tp9.util.EstadoLibro;

@SpringBootTest
public class LibroServiceTest {
	
	@Autowired
	ILibroService libroService;
	
	ModelMapper mapper = new ModelMapper();
	
	LibroDto libroGuardarDto;
	LibroDto libroThrowDto1;
	LibroDto libroThrowDto2;
	LibroDto libroGuardadoDto;
	
	@BeforeEach
	void iniciarVariables() {
		libroGuardarDto = new LibroDto();
		libroGuardarDto.setTitulo("Un libro");
		libroGuardarDto.setAutor("autor");
		libroGuardarDto.setIsbn("ISBN-10-1234567890");
		libroGuardarDto.setNumeroInventario(111l);
		
		libroThrowDto1 = new LibroDto();
		libroThrowDto1.setTitulo("Otro libro");
		libroThrowDto1.setAutor("autor");
		libroThrowDto1.setIsbn("ISBN-10-1234567890");
		libroThrowDto1.setNumeroInventario(222l);
		
		libroThrowDto2 = new LibroDto();
		libroThrowDto2.setTitulo("Otro libro");
		libroThrowDto2.setAutor("autor");
		libroThrowDto2.setIsbn("ISBN-10-0987654321");
		libroThrowDto2.setNumeroInventario(111l);
		
		libroGuardadoDto = new LibroDto();
	}
	
	@AfterEach
	void resetearVariables() {
		libroGuardarDto = null;
		libroThrowDto1 = null;
		libroThrowDto2 = null;
		libroGuardadoDto = null;
	}
	
	@Test
	@DisplayName("Test guardar libro")
	void guardarLibroTest() throws ManagerException {
		libroService.guardarLibro(libroGuardarDto);
		assertEquals(6, libroService.librosSize());
		
		libroGuardadoDto = libroService.buscarLibroPorTitulo("Un libro");
		assertNotNull(libroGuardadoDto);
		assertEquals(libroGuardadoDto.getIsbn(), "ISBN-10-1234567890");
		
		assertThrows(ManagerException.class,()->libroService.guardarLibro(libroThrowDto1));
		assertThrows(ManagerException.class,()->libroService.guardarLibro(libroThrowDto2));
		
		libroService.eliminarLibro(libroGuardadoDto.getId());
	}
	
	@Test
	@DisplayName("Test eliminar libro")
	void eliminarLibroTest() throws ManagerException {
		libroService.guardarLibro(libroGuardarDto);
		libroGuardadoDto = libroService.buscarLibroPorTitulo("Un libro");
		
		libroService.eliminarLibro(libroGuardadoDto.getId());
		assertEquals(5, libroService.librosSize());
		
		libroGuardadoDto = libroService.buscarLibroPorTitulo("Un libro");
		assertNull(libroGuardadoDto);
	}
	
	@Test
	@DisplayName("Test editar libro")
	void editarLibroTest() {
		libroGuardadoDto = libroService.buscarLibroPorTitulo("IT");
		
		libroGuardadoDto.setTitulo("Moby dick");
		libroGuardadoDto.setEstado(EstadoLibro.PRESTADO.toString());
		libroService.editarLibro(libroGuardadoDto);
		
		libroGuardadoDto = libroService.buscarLibroPorAutor("Stephen King");
		assertNotNull(libroGuardadoDto);
		assertEquals(libroGuardadoDto.getTitulo(), "Moby dick");
		assertEquals(libroGuardadoDto.getEstado(), EstadoLibro.PRESTADO.toString());
	}
	
	@Test
	@DisplayName("Test Buscar Libro por Autor")
	void buscarLibroAutorTest() {
		libroGuardadoDto = libroService.buscarLibroPorAutor("J.K Rowling");
		assertNotNull(libroGuardadoDto);
		
		assertEquals(libroGuardadoDto.getTitulo(), "Harry Potter y la piedra filosofal");
		assertEquals(libroGuardadoDto.getEstado(), EstadoLibro.DISPONIBLE.toString());
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
