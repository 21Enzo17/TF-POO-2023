package ar.edu.unju.fi.tp9.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.edu.unju.fi.tp9.dto.AlumnoDto;
import ar.edu.unju.fi.tp9.dto.PrestamoDto;
import ar.edu.unju.fi.tp9.dto.PrestamoInfoDto;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.service.ILibroService;
import ar.edu.unju.fi.tp9.service.IMiembroService;
import ar.edu.unju.fi.tp9.service.IPrestamoService;

@SpringBootTest
class IPrestamoServiceTest {

    @Autowired
    IPrestamoService target;

    @Autowired
    IMiembroService miembroService;

    @Autowired
    ILibroService libroService;

    static AlumnoDto alumnoDto;
    static PrestamoDto prestamo;
    static PrestamoDto prestamo2;
    static PrestamoInfoDto prestamoInfo;

    @BeforeEach
    public void setUp() throws ManagerException {
        prestamo = new PrestamoDto();
        prestamo.setEstado("PRESTADO");
        prestamo.setFechaDevolucion("10/06/2023 - 18:00");
        prestamo.setFechaPrestamo("05/06/2023 - 18:00");
        prestamo.setIdLibroDto(libroService.buscarLibroPorTitulo("Harry Potter y la piedra filosofal").getId());

        prestamo2 = new PrestamoDto();
        prestamo2.setEstado("PRESTADO");
        prestamo2.setFechaDevolucion("10/06/2023 - 18:00");
        prestamo2.setFechaPrestamo("05/06/2023 - 18:00");
        prestamo2.setIdMiembroDto(miembroService.obtenerMiembroByCorreo("enzo.meneghini@hotmail.com").getId());
        prestamo2.setIdLibroDto(libroService.buscarLibroPorTitulo("IT").getId());

    }


    @AfterEach
    public void tearDown() {
        prestamo = null;
        alumnoDto = null;
        prestamoInfo = null;
        target = null;

    }

    @Test
    void guardarPrestamoTest() throws ManagerException {
        alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Juan Perez");
        alumnoDto.setCorreo("enzo.meneghini@fi.unju.edu.ar");
        alumnoDto.setNumeroTelefonico("123456789");
        alumnoDto.setLibretaUniversitaria("1234");
        // Chequeo de envio de correo y guardo exitoso
        assertDoesNotThrow(()->prestamoInfo = target.guardarPrestamo(miembroService.guardarMiembro(alumnoDto).getId(),libroService.buscarLibroPorTitulo("Harry Potter y la piedra filosofal").getId()));
        assertEquals(prestamoInfo.getNombreMiembro(), "Juan Perez");
        assertEquals(prestamoInfo.getTituloLibro(), "Harry Potter y la piedra filosofal");
        // Intento guardarlo devuelta para que detecte el libro ya prestado
        assertThrows(ManagerException.class, ()->target.guardarPrestamo(miembroService.guardarMiembro(alumnoDto).getId(),libroService.buscarLibroPorTitulo("Harry Potter y la piedra filosofal").getId()));
        // Intento de save con miembro bloqueado
        assertThrows(ManagerException.class, ()->target.guardarPrestamo(2l,libroService.buscarLibroPorTitulo("IT").getId()));
    }

    @Test
    void devolverLibro() throws ManagerException{
        prestamoInfo = target.guardarPrestamo(miembroService.obtenerMiembroByCorreo("enzo.meneghini@hotmail.com").getId(),libroService.buscarLibroPorTitulo("IT").getId());
        target.devolucionPrestamo(prestamoInfo.getId());
        assertEquals("DEVUELTO", target.obtenerPrestamoById(prestamoInfo.getId()).getEstado());
    }
}
