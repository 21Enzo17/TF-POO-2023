package ar.edu.unju.fi.tp9.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDate;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.edu.unju.fi.tp9.dto.PrestamoDto;
import ar.edu.unju.fi.tp9.entity.Prestamo;
import ar.edu.unju.fi.tp9.service.IPdfGenerator;

@SpringBootTest
public class PdfGeneratorTest {

    @Autowired
    IPdfGenerator pdfGenerator;

    static String htmlBody;
    static PrestamoDto prestamo;

    @BeforeEach
    public void setUp() throws Exception {
        htmlBody = "<html><body><div style='text-align: center;'>" +
            "<h1>Bibliowlteca</h1>" +
            "<img src=\"../../../../../../../../main/resources/images/Bibliowlteca.png\" style=\"width: 200px; display: block; margin: 0 auto; border-radius: 20px;\" />"  +
            "<h2>Informacion del prestamo</h2>"  +
            "<p><b>Titulo:</b> "+ "Macarena" + "</p>"+ 
            "<p><b>ISBN:</b> " + "12345" + "</p>" +
            "<p><b>Fecha de prestamo:</b> "+ LocalDate.now()  + "</p>" +
            "<p><b>Fecha de devolucion:</b> "+ LocalDate.now().plusDays(7) + "</p>" +
            "<h6>Correo generado automaticamente</h6>" + 
            "</div></body></html>";
        
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void generarPdfTest() throws Exception {
        assertDoesNotThrow(()-> pdfGenerator.generarPdf(htmlBody) );
    }
}
