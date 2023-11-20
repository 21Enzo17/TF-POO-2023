package ar.edu.unju.fi.tp9.service;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;



import ar.edu.unju.fi.tp9.dto.PrestamoDto;

public interface IPdfGenerator {

    public void generarPdf(String html) throws FileNotFoundException;

    public ByteArrayOutputStream generarPdfSinGuardar(String html);
} 
