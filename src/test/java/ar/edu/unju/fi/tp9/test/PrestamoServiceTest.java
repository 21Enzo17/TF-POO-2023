package ar.edu.unju.fi.tp9.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.edu.unju.fi.tp9.dto.LibroBuscarDto;
import ar.edu.unju.fi.tp9.dto.PrestamoDto;
import ar.edu.unju.fi.tp9.entity.util.EstadoPrestamo;
import ar.edu.unju.fi.tp9.service.LibroService;
import ar.edu.unju.fi.tp9.service.PrestamoService;

@SpringBootTest
public class PrestamoServiceTest {

	@Autowired
	PrestamoService prestamoService;
	
	@Autowired
	LibroService libroService;
	
	PrestamoDto prestamoDto;
	LibroBuscarDto libroDto;
	
	@BeforeEach
	void inicializarVariables() {
		libroDto = libroService.buscarLibroPorTitulo("IT");
		
		prestamoDto = new PrestamoDto();
		prestamoDto.setFechaPrestamo("06/11/2023 17:30");
		prestamoDto.setFechaDevolucion("13/11/2023 17:30");
		prestamoDto.setLibro(libroDto);
	}
	
	@AfterEach
	void reiniciarVariables() {
		libroDto = null;
		prestamoDto = null;
	}
	
	@Test
	@DisplayName("Test Guardar Prestamo")
	void guardarPrestamoTest() {
		prestamoService.guardarPrestamo(prestamoDto);
		assertEquals(1, prestamoService.getPrestamosSize());
		
		prestamoDto = prestamoService.buscarPrestamoPorId(1l);
		assertNotNull(prestamoDto);
		assertEquals(prestamoDto.getEstado(), "PRESTADO");
		assertEquals(prestamoDto.getLibro().getTitulo(), "Libro test");
		
		prestamoService.eliminarPrestamo(prestamoDto);
	}
	
	@Test
	@DisplayName("Test Devolver Prestamo")
	void devolverPrestamoTest() {
		prestamoService.guardarPrestamo(prestamoDto);
		prestamoDto = prestamoService.buscarPrestamoPorId(1l);
		
		prestamoService.devolverPrestamo(prestamoDto);
		prestamoDto = prestamoService.buscarPrestamoPorId(1l);
		assertEquals(prestamoDto.getEstado(), EstadoPrestamo.DEVUELTO.toString());
		
		prestamoService.eliminarPrestamo(prestamoDto);
	}
}
