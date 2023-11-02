package ar.edu.unju.fi.tp9;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.edu.unju.fi.tp9.dto.AlumnoDto;
import ar.edu.unju.fi.tp9.dto.MiembroDto;
import ar.edu.unju.fi.tp9.dto.PrestamoDto;
import ar.edu.unju.fi.tp9.service.IMiembroService;
import ar.edu.unju.fi.tp9.service.IPrestamoService;

@SpringBootTest
public class IPrestamoServiceTest {

    @Autowired
    IPrestamoService target;

    @Autowired
    IMiembroService miembroService;

    static AlumnoDto alumnoDto;
    static PrestamoDto prestamo;

    @BeforeEach
    public void setUp() {
        alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Juan Perez");
        alumnoDto.setCorreo("juan@gmail.com");
        alumnoDto.setNumeroTelefonico("123456789");
        alumnoDto.setLibretaUniversitaria("1234");
        miembroService.guardarMiembro(alumnoDto);

        prestamo = new PrestamoDto();
        prestamo.setEstado("PRESTADO");
        prestamo.setFechaDevolucion("2023-06-05T18:00");
        prestamo.setFechaPrestamo("2023-06-10T18:00");
        prestamo.setMiembroDto(miembroService.obtenerMiembroByCorreo("juan@gmail.com"));


    }


    @AfterEach
    public void tearDown() {
        prestamo = null;
        alumnoDto = null;
        target = null;

    }

    @Test
    public void guardarPrestamoTest() {
        target.guardarPrestamo(prestamo);
        assertEquals(prestamo.getMiembroDto().getCorreo(), 
        target.buscarPrestamoPorMiembro(miembroService.obtenerMiembroByCorreo("juan@gmail.com")).getMiembroDto().getCorreo());
    }

    @Test
    public void devolverLibro(){
        target.guardarPrestamo(prestamo);
        prestamo =  target.buscarPrestamoPorMiembro(miembroService.obtenerMiembroByCorreo("juan@gmail.com"));
        target.devolucionPrestamo(prestamo);
        assertEquals("DEVUELTO", target.buscarPrestamoPorMiembro(miembroService.obtenerMiembroByCorreo("juan@gmail.com")).getEstado());
    }

}
