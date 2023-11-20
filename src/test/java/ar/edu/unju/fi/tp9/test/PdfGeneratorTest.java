package ar.edu.unju.fi.tp9.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.edu.unju.fi.tp9.dto.PrestamoDto;
import ar.edu.unju.fi.tp9.entity.Prestamo;
import ar.edu.unju.fi.tp9.service.IComprobanteGenerator;
import ar.edu.unju.fi.tp9.util.DateFormatter;

@SpringBootTest
public class PdfGeneratorTest {

    @Autowired
    IComprobanteGenerator pdfGenerator;
    @Autowired
    DateFormatter dateFormatter;

    static String htmlBody;
    static PrestamoDto prestamo;

    @BeforeEach
    public void setUp() throws Exception {
        URL imageUrl = getClass().getResource("/images/Bibliowlteca.png");
        String imgTag = "<img src=\"" + imageUrl.toString() + "\" style=\"width: 150px; display: block; margin: 0 auto; border-radius: 20px;\" />";
        htmlBody = "<html><body><div style='text-align: center; border: 2px solid black; padding: 10px; width: 50%; margin: auto; display: block; border-radius:25px'>" +
        "<h1>Bibliowlteca</h1>" +
        imgTag +
        "<hr>"+
        "<h2>Comprobante de Prestamo <br> de un Libro</h2>"  +
        "<p>Emitido el: " + dateFormatter.transformarFechaNatural(LocalDateTime.now().withSecond(0).withNano(0).toString())  + "</p>" +
        "<hr>"+
        "<div style='text-align: justify;'>" +
            "<p><b>Nombre del miembro:</b> "+ "Juan Perez" + "</p>"+
            "<p><b>Correo:</b> "+ "enzo.meneghini@hotmail.com" + "</p>"+
            "<p><b>Titulo:</b> "+ "Macarena" + "</p>"+ 
            "<p><b>ISBN:</b> " + "12345" + "</p>" +
            "<p><b>Fecha de prestamo:</b> "+ LocalDate.now()  + "</p>" +
            "<p><b>Fecha de devolucion:</b> "+ LocalDate.now().plusDays(7) + "</p>" +
            "</div>" +
            "<h6>Comprobante generado automaticamente</h6>" + 
        "</div></body></html>";
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void generarPdfTest() throws Exception {
        assertNotNull(pdfGenerator.generarComprobante(htmlBody));
    }
}
