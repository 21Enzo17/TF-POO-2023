package ar.edu.unju.fi.tp9.service.imp;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import ar.edu.unju.fi.tp9.service.IPdfGenerator;

@Service
public class PdfGenerator implements IPdfGenerator {
    static Logger logger = Logger.getLogger(PdfGenerator.class);
    
    @Override
    public void generarPdf(String html) throws FileNotFoundException {
        String dest = "prestamo.pdf";
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdfDoc = new PdfDocument(writer);
        ConverterProperties properties = new ConverterProperties();
        HtmlConverter.convertToPdf(html, pdfDoc, properties);
        pdfDoc.close();
        logger.info("PDF generado con exito");
        
    }


    /**
     * Este metodo se encarga de generar un pdf sin guardarlo en el sistema de archivos,
     * retornando un ByteArrayOutputStream con el pdf generado.
     * @param html
     * @return ByteArrayOutputStream
     * @throws FileNotFoundException
     */
    @Override
    public ByteArrayOutputStream generarPdfSinGuardar(String html) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
    
        PdfDocument pdfDoc = new PdfDocument(writer);
    
        ConverterProperties properties = new ConverterProperties();
        HtmlConverter.convertToPdf(html, pdfDoc, properties);
    
        return outputStream;
    }

}
