package ar.edu.unju.fi.tp9;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.edu.unju.fi.tp9.dto.AlumnoDto;
import ar.edu.unju.fi.tp9.dto.DocenteDto;
import ar.edu.unju.fi.tp9.dto.MiembroDto;
import ar.edu.unju.fi.tp9.exceptions.ModelException;
import ar.edu.unju.fi.tp9.service.IMiembroService;

@SpringBootTest
public class IMiembroServiceTest {
    static Logger logger = LogManager.getLogger(IMiembroServiceTest.class);

    @Autowired
    IMiembroService target;
    
    static AlumnoDto alumnoDto;
    static DocenteDto docenteDto;
    static MiembroDto aux;
    
    @BeforeEach
    public void setUp(){
        alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Juan Perez");
        alumnoDto.setCorreo("juan@gmail.com");
        alumnoDto.setNumeroTelefonico("123456789");
        alumnoDto.setLibretaUniversitaria("1234");
        
        docenteDto = new DocenteDto();
        docenteDto.setNombre("Manuel Lopez");
        docenteDto.setCorreo("manuel@gmail.com");
        docenteDto.setNumeroTelefonico("987654321");
        docenteDto.setLegajo("4567");

        aux = new MiembroDto();
    }

    @AfterEach
    public void tearDown(){
        logger.info("Ejecutando tear down");
        alumnoDto = null;
        docenteDto = null;
        aux = null;
        target = null;

    }

    /**
     * Este test prueba guardar un docente y un alumno en la base de datos, luego se verifica que se hayan guardado
     * correctamente, ademas se testea que no se pueda guardar un miembro con el mismo correo.
     */
    @Test
    public void testGuardarMiembro() {
        logger.info("IMiembroServiceTest: testGuardarMiembro");
        target.guardarMiembro(alumnoDto);
        target.guardarMiembro(docenteDto);
        assertEquals(alumnoDto.getCorreo(), target.obtenerMiembroByCorreo(alumnoDto.getCorreo()).getCorreo());
        assertEquals(docenteDto.getCorreo(), target.obtenerMiembroByCorreo(docenteDto.getCorreo()).getCorreo());
        // Intento guardar el mismo alumno otra vez, para controlar la exepcion
        assertThrows(ModelException.class, () -> target.guardarMiembro(alumnoDto));
        target.eliminarMiembro(alumnoDto);
        target.eliminarMiembro(docenteDto);
    }

    /**
     * Este test prueba eliminar un miembro de la base de datos, luego se verifica que se haya eliminado correctamente,
     */
    @Test
    public void testEliminarMiembro(){
        logger.info("IMiembroServiceTest: testEliminarMiembro");
        target.guardarMiembro(alumnoDto);
        assertEquals(alumnoDto.getCorreo(), target.obtenerMiembroByCorreo(alumnoDto.getCorreo()).getCorreo());
        target.eliminarMiembro(alumnoDto);
        assertThrows(ModelException.class, () -> target.obtenerMiembroByCorreo(alumnoDto.getCorreo()));
    }

    /**
     * Este test prueba obtener un miembro de la base de datos por su correo.
     */
    @Test
    public void testObtenerMiembroByCorreo(){
        logger.info("IMiembroServiceTest: testObtenerMiembroByCorreo");
        target.guardarMiembro(alumnoDto);
        assertEquals(alumnoDto.getCorreo(), target.obtenerMiembroByCorreo(alumnoDto.getCorreo()).getCorreo());
        target.eliminarMiembro(alumnoDto);
    }


    /**
     * Este test prueba modificar un miembro de la bd, tengase en cuenta que antes debo obtener el id asignado por la bd
     * por lo cual uso el obtenerMiembroByCorreo antes de editar el miembro, tambien se comprueba que en caso de modificar
     * el correo, este no exista en la base de datos
     */
    @Test
    public void modificarMiembro(){
        logger.info("IMiembroServiceTest: modificarMiembro");
        target.guardarMiembro(alumnoDto);
        target.guardarMiembro(docenteDto);
        assertEquals(alumnoDto.getCorreo(), target.obtenerMiembroByCorreo(alumnoDto.getCorreo()).getCorreo());
        alumnoDto = (AlumnoDto)  target.obtenerMiembroByCorreo(alumnoDto.getCorreo()); // obtengo el id
        alumnoDto.setCorreo("enzo@gmail.com"); // cambio el correo
        target.modificarMiembro(alumnoDto); // modifico
        assertEquals(alumnoDto.getCorreo(),target.obtenerMiembroById(alumnoDto.getId()).getCorreo());
        aux = (DocenteDto) target.obtenerMiembroByCorreo(docenteDto.getCorreo()); // obtengo el id
        aux.setCorreo("enzo@gmail.com"); // cambio el correo
        assertThrows(ModelException.class, () -> target.modificarMiembro(aux)); 
        target.eliminarMiembro(alumnoDto);
        target.eliminarMiembro(docenteDto);

    }




}
