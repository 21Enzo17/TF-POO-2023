package ar.edu.unju.fi.tp9.service.imp;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import ar.edu.unju.fi.tp9.service.IComprobanteGenerator;

@Service
public class ComprobanteGenerator implements IComprobanteGenerator {
    static Logger logger = Logger.getLogger(ComprobanteGenerator.class);
    

    /**
     * Este metodo se encarga de generar un pdf sin guardarlo en el sistema de archivos,
     * retornando un ByteArrayOutputStream con el pdf generado.
     * @param html
     * @return ByteArrayOutputStream
     * @throws FileNotFoundException
     */
    @Override
    public ByteArrayOutputStream generarComprobante(String html) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
    
        PdfDocument pdfDoc = new PdfDocument(writer);
    
        ConverterProperties properties = new ConverterProperties();
        HtmlConverter.convertToPdf(html, pdfDoc, properties);
        
        logger.info("Comprobante generado correctamente");
        return outputStream;
    }

}
