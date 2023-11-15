package ar.edu.unju.fi.tp9.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.edu.unju.fi.tp9.dto.AlumnoDto;
import ar.edu.unju.fi.tp9.dto.PrestamoDto;
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
        target = null;

    }

    @Test
    void guardarPrestamoTest() throws ManagerException {
        alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Juan Perez");
        alumnoDto.setCorreo("enzo.meneghini@fi.unju.edu.ar");
        alumnoDto.setNumeroTelefonico("123456789");
        alumnoDto.setLibretaUniversitaria("1234");
        miembroService.guardarMiembro(alumnoDto);
        prestamo.setIdMiembroDto(miembroService.obtenerMiembroByCorreo("enzo.meneghini@fi.unju.edu.ar").getId());
        target.guardarPrestamo(prestamo);
        assertEquals(prestamo.getIdMiembroDto(), 
        target.buscarPrestamoPorMiembro(miembroService.obtenerMiembroByCorreo("enzo.meneghini@fi.unju.edu.ar")).getIdMiembroDto());
    }

    @Test
    void devolverLibro() throws ManagerException{
        target.guardarPrestamo(prestamo2);
        prestamo2 = target.buscarPrestamoPorMiembro(miembroService.obtenerMiembroByCorreo("enzo.meneghini@hotmail.com"));
        target.devolucionPrestamo(prestamo2);
        assertEquals("DEVUELTO", target.buscarPrestamoPorMiembro(miembroService.obtenerMiembroByCorreo("enzo.meneghini@hotmail.com")).getEstado());
    }

}
