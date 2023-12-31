package ar.edu.unju.fi.tp9.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;

import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.service.IEmailService;
import ar.edu.unju.fi.tp9.service.IPrestamoService;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    IEmailService target;
    @Autowired
    IPrestamoService prestamoService;
    String para;
    String tema;
    String htmlBody;

    @BeforeEach
    public void setUp(){
        para = "enzo.meneghinifenix@gmail.com";
        tema = "Prestamo: " + "Titulo1";

        htmlBody = "<html><body><div style='text-align: center;'>" +
            "<h1>Bibliowlteca</h1>" +
            "<img src=\"cid:logo\" style=\"width: 200px; display: block; margin: 0 auto; border-radius: 20px;\" />"  +
            "<h2>Informacion del prestamo</h2>"  +
            "<p><b>Titulo:</b> "+ "Titulo1" + "</p>"+ 
            "<p><b>ISBN:</b> " + "15616510651" + "</p>" +
            "<p><b>Fecha de prestamo:</b> "+ "08/11/2023 - 18:45"  + "</p>" +
            "<p><b>Fecha de devolucion:</b> "+ "18/11/2023 - 18:45" + "</p>" +
            "<h6>Correo generado automaticamente</h6>" + 
            "</div></body></html>";
    }

    @AfterEach
    public void tearDown(){
        para= null;
        tema = null;
        htmlBody = null;
        target = null;
    }


    /**
     * En este test, se testea el correcto envio del mail y el error en caso de pasar un parametro incorrecto.
     */
    @Test
    void envio(){
        InputStreamSource inputStream = new FileSystemResource("src/main/resources/images/Bibliowlteca.png");
        assertDoesNotThrow(()->target.send("poo2023correo@gmail.com",para, tema, htmlBody,inputStream,prestamoService.generarComprobante(1l)));
        para= "123gmail.com"; // Se asigna un correo invalido
        assertThrows(ManagerException.class, ()-> target.send("poo2023correo@gmail.com",para, tema, htmlBody,inputStream,prestamoService.generarComprobante(1l)));
        // Se obtiene una excepcion personalizada
    }
}
