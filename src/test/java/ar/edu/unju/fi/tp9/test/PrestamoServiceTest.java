package ar.edu.unju.fi.tp9.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.edu.unju.fi.tp9.dto.LibroDto;
import ar.edu.unju.fi.tp9.dto.PrestamoDto;
import ar.edu.unju.fi.tp9.entity.util.EstadoPrestamo;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.service.LibroService;
import ar.edu.unju.fi.tp9.service.PrestamoService;
import ar.edu.unju.fi.tp9.util.EstadoLibro;

@SpringBootTest
public class PrestamoServiceTest {

	@Autowired
	PrestamoService prestamoService;
	
	@Autowired
	static LibroService libroService;
	
	PrestamoDto prestamoDto;
	
	@Test
	@DisplayName("Test Guardar Prestamo")
	void guardarPrestamoTest() {
		prestamoService.guardarPrestamo(prestamoDto);
		assertEquals(1, prestamoService.getPrestamosSize());
		
		prestamoDto = prestamoService.buscarPrestamoPorId(1l);
		assertNotNull(prestamoDto);
		assertEquals(prestamoDto.getEstado(), "PRESTADO");
		assertEquals(prestamoDto.getLibro().getTitulo(), "libro uno");
		
		prestamoService.eliminarPrestamo(prestamoDto);
	}
	
	@Test
	@DisplayName("Test Devolver Prestamo")
	void devolverPrestamoTest() {
		prestamoDto.setLibro(libroService.buscarLibroPorTitulo("Don Quijote de la Mancha"));
		prestamoService.guardarPrestamo(prestamoDto);
		prestamoDto = prestamoService.buscarPrestamoPorId(1l);
		
		prestamoService.devolverPrestamo(prestamoDto);
		prestamoDto = prestamoService.buscarPrestamoPorId(1l);
		assertEquals(prestamoDto.getEstado(), EstadoPrestamo.DEVUELTO.toString());
		
		prestamoService.eliminarPrestamo(prestamoDto);
	}
}
