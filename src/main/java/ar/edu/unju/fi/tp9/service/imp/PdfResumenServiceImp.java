package ar.edu.unju.fi.tp9.service.imp;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.apache.log4j.Logger;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import ar.edu.unju.fi.tp9.dto.PrestamoInfoDto;
import ar.edu.unju.fi.tp9.service.IResumenService;
import ar.edu.unju.fi.tp9.util.DateFormatter;

@Service("pdfService")
public class PdfResumenServiceImp implements IResumenService {
    Logger logger =  Logger.getLogger(this.getClass());
	
	@Autowired
	DateFormatter dateFormatter;
	
	@Override
	public ResponseEntity<byte[]> realizarResumen(List<PrestamoInfoDto> prestamosDto, String fechaInicio, String fechaFin)  {
		ByteArrayOutputStream resumen = new ByteArrayOutputStream();
		
		PdfWriter writer = new PdfWriter(resumen);
		PdfDocument pdfDoc = new PdfDocument(writer);
		
		pdfDoc.addNewPage();
		Document document = new Document(pdfDoc);
		
		agregarImagen(document);
		crearCuerpo(document, fechaInicio, fechaFin);
		crearTabla(document, prestamosDto);	
		document.close();
		
		byte[] pdfBytes = resumen.toByteArray();
        return new ResponseEntity<>(pdfBytes, crearHeaders(pdfBytes), HttpStatus.OK);
	}
	
	private void agregarImagen(Document document) {
		String path = "src/main/resources/images/Bibliowlteca.png";
		ImageData data;
		
		try {
			data = ImageDataFactory.create(path);
			Image image = new Image(data);
			image.setPadding(50);
			image.setWidth(150);
			image.setMaxHeight(200);
			image.setAutoScale(false);
			
			Paragraph paragraph = new Paragraph();
	        paragraph.add(image);
	        paragraph.setTextAlignment(TextAlignment.CENTER);
	        paragraph.setHorizontalAlignment(HorizontalAlignment.CENTER);
	        
	        document.add(paragraph);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private void crearCuerpo(Document document, String fechaInicio, String fechaFin) {
		String content = "RESUMEN DE PRESTAMOS\nEntre " + fechaInicio + " y " + fechaFin;
		Paragraph paragraph = new Paragraph(content);
		paragraph.setFontSize(20);
		paragraph.setTextAlignment(TextAlignment.CENTER);
		paragraph.setMargin(10);
		paragraph.setPaddingLeft(10);
		paragraph.setPaddingRight(10);
		paragraph.setWidth(1000);
		paragraph.setHeight(100);
		document.add(paragraph);
	}
	
	private void crearTabla(Document document, List<PrestamoInfoDto> prestamosDto) {
		float [] pointColumnWidths = {100F, 150F, 150F, 150F, 150F, 150F};
		Table table = new Table(pointColumnWidths);
        logger.debug("Creando tabla con " + prestamosDto.size() + " prestamos");
		table.addCell(new Cell().add(new Paragraph("Id")));
		table.addCell(new Cell().add(new Paragraph("Miembro")));
		table.addCell(new Cell().add(new Paragraph("Libro")));
		table.addCell(new Cell().add(new Paragraph("Fecha Prestamo")));
		table.addCell(new Cell().add(new Paragraph("Fecha Devolucion")));
		table.addCell(new Cell().add(new Paragraph("Estado")));
		logger.info("Se creo la tabla");
		
		for(PrestamoInfoDto dto : prestamosDto) {
			table.addCell(new Cell().add(new Paragraph(dto.getId().toString())));
			table.addCell(new Cell().add(new Paragraph(dto.getNombreMiembro())));
			table.addCell(new Cell().add(new Paragraph(dto.getTituloLibro())));
			table.addCell(new Cell().add(new Paragraph(dto.getFechaPrestamo())));
			table.addCell(new Cell().add(new Paragraph(dto.getFechaDevolucion())));
			table.addCell(new Cell().add(new Paragraph(dto.getEstado())));
			logger.info("Se agrego un prestamo");
		}
		logger.info("Se agrego la tabla correctamente");
        document.add(table);
	}

	private HttpHeaders crearHeaders(byte[] pdfBytes) {
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDispositionFormData("attachment", "Resumen.pdf");
	    headers.setContentLength(pdfBytes.length);
	    return headers;
	}
}
