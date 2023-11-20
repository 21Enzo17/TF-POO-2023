package ar.edu.unju.fi.tp9.util;

import java.net.URL;
import java.time.LocalDateTime;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.unju.fi.tp9.dto.LibroDto;
import ar.edu.unju.fi.tp9.dto.MiembroDto;

@Component
public  class BodyGenerator {

    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    DateFormatter dateFormatter;

    /**
     * Metodo que genera el body del correo
     * @param libroDto
     * @param fechaPrestamo
     * @param fechaDevolucion
     * @return
     */
    public String generarBodyCorreo(LibroDto libroDto, String fechaPrestamo, String fechaDevolucion){
        String htmlBody = "<html><body><div style='text-align: center;'>" +
            "<h1>Bibliowlteca</h1>" +
            "<img src=\"cid:logo\" style=\"width: 200px; display: block; margin: 0 auto; border-radius: 20px;\" />"  +
            "<h2>Informacion del prestamo</h2>"  +
            "<p><b>Titulo:</b> "+ libroDto.getTitulo() + "</p>"+ 
            "<p><b>ISBN:</b> " + libroDto.getIsbn() + "</p>" +
            "<p><b>Fecha de prestamo:</b> "+ fechaPrestamo  + "</p>" +
            "<p><b>Fecha de devolucion:</b> "+ fechaDevolucion + "</p>" +
            "<h6>Correo generado automaticamente</h6>" + 
            "</div></body></html>";
            logger.info("Body generado correctamente");
        return htmlBody;
    }


    /**
     * Metodo que genera el body del comprobante
     * @param miembroDto
     * @param libroDto
     * @param fechaPrestamo
     * @param fechaDevolucion
     * @return
     */
    public String generarBodyComprobante(MiembroDto miembroDto,LibroDto libroDto, String fechaPrestamo, String fechaDevolucion,String estado){
        URL imageUrl = getClass().getResource("/images/Bibliowlteca.png");
        String imgTag = "<img src=\"" + imageUrl.toString() + "\" style=\"width: 150px; display: block; margin: 0 auto; border-radius: 20px;\" />";
        return  "<html><body><div style='text-align: center; border: 2px solid black; padding: 10px; width: 50%; margin: auto; display: block; border-radius:25px'>" +
        "<h1>Bibliowlteca</h1>" +
        imgTag +
        "<hr>"+
        "<h2>Comprobante de Prestamo <br> de un Libro</h2>"  +
        "<p>Emitido el: " + dateFormatter.transformarFechaNatural(LocalDateTime.now().withSecond(0).withNano(0).toString())  + "</p>" +
        "<hr>"+
        "<div style='text-align: justify;'>" +
            "<p><b>Nombre del miembro:</b> "+ miembroDto.getNombre() + "</p>"+
            "<p><b>Correo:</b> "+ miembroDto.getCorreo() + "</p>"+
            "<p><b>Titulo:</b> "+ libroDto.getTitulo() + "</p>"+ 
            "<p><b>ISBN:</b> " + libroDto.getIsbn() + "</p>" +
            "<p><b>Fecha de prestamo:</b> "+ fechaPrestamo  + "</p>" +
            "<p><b>Fecha de devolucion:</b> "+ fechaDevolucion + "</p>" +
            "<p><b>Estado:</b> "+ estado + "</p>" +
            "</div>" +
            "<h6>Comprobante generado automaticamente</h6>" + 
        "</div></body></html>";
    }
}
