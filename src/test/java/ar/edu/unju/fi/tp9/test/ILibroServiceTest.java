package ar.edu.unju.fi.tp9.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.edu.unju.fi.tp9.dto.LibroDto;
import ar.edu.unju.fi.tp9.enums.EstadoLibro;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.service.ILibroService;

@SpringBootTest
class ILibroServiceTest {
	
	@Autowired
	ILibroService libroService;
	
	ModelMapper mapper = new ModelMapper();
	
	LibroDto libroGuardarDto;
	LibroDto libroThrowDto;
	LibroDto libroGuardadoDto;
	List<LibroDto> librosPorAutor;
	List<LibroDto> librosPorIsbn;
	
	@BeforeEach
	void iniciarVariables() {
		libroGuardarDto = new LibroDto();
		libroGuardarDto.setTitulo("Un libro");
		libroGuardarDto.setAutor("autor");
		libroGuardarDto.setIsbn("ISBN-10-1234567890");
		libroGuardarDto.setNumeroInventario(111l);
		
		libroThrowDto = new LibroDto();
		libroThrowDto.setTitulo("Otro libro");
		libroThrowDto.setAutor("autor");
		libroThrowDto.setIsbn("ISBN-10-1234567890");
		libroThrowDto.setNumeroInventario(111l);
		
		libroGuardadoDto = new LibroDto();
		librosPorAutor = new ArrayList<>();
		librosPorIsbn = new ArrayList<>();
	}
	
	@AfterEach
	void resetearVariables() {
		libroGuardarDto = null;
		libroThrowDto = null;
		libroGuardadoDto = null;
		librosPorAutor = null;
	}
	
	@Test
	@DisplayName("Test guardar libro")
	void guardarLibroTest() throws ManagerException {
		libroService.guardarLibro(libroGuardarDto);
		assertEquals(6, libroService.librosSize());
		
		libroGuardadoDto = libroService.buscarLibroPorTitulo("Un libro");
		assertNotNull(libroGuardadoDto);
		assertEquals(libroGuardadoDto.getIsbn(), "ISBN-10-1234567890");
		
		assertThrows(ManagerException.class,()->libroService.guardarLibro(libroThrowDto));
		
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
		
		librosPorAutor = libroService.buscarLibroPorAutor("Stephen King");
		assertEquals(librosPorAutor.size(), 1);
		assertEquals(librosPorAutor.get(0).getTitulo(), "Moby dick");
		assertEquals(librosPorAutor.get(0).getEstado(), EstadoLibro.PRESTADO.toString());
	}
	
	@Test
	@DisplayName("Test Buscar Libro por Autor")
	void buscarLibroAutorTest() {
		librosPorAutor = libroService.buscarLibroPorAutor("J.K Rowling");
		assertEquals(librosPorAutor.size(), 1);
		
		assertEquals(librosPorAutor.get(0).getTitulo(), "Harry Potter y la piedra filosofal");
		assertEquals(librosPorAutor.get(0).getEstado(), EstadoLibro.DISPONIBLE.toString());
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
		librosPorIsbn = libroService.buscarLibroPorIsbn("ISBN-10-0451524935");
		assertEquals(librosPorIsbn.size(), 1);
		
		assertEquals(librosPorIsbn.get(0).getAutor(), "J.R.R. Tolkien");
		assertEquals(librosPorIsbn.get(0).getTitulo(), "El senior de los anillos");
	}
}
