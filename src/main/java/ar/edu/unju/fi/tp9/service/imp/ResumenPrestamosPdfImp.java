package ar.edu.unju.fi.tp9.service.imp;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import ar.edu.unju.fi.tp9.entity.Prestamo;
import ar.edu.unju.fi.tp9.repository.PrestamoRepository;
import ar.edu.unju.fi.tp9.service.IResumenPrestamos;
import ar.edu.unju.fi.tp9.util.DateFormatter;

@Service
public class ResumenPrestamosPdfImp implements IResumenPrestamos{
	
	@Autowired
	PrestamoRepository prestamoRepository;
	
	@Autowired
	DateFormatter dateFormatter;
	
	@Override
	public ResponseEntity<byte[]> realizarResumen(String fechaInicio, String fechaFinal) throws FileNotFoundException {
		String dest = "Resumen.pdf";
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		LocalDateTime fechaInicioFormateada = dateFormatter.fechDateTime(fechaInicio);
		LocalDateTime fechaFinalFormateada = dateFormatter.fechDateTime(fechaFinal);
		
		List<Prestamo> prestamos = prestamoRepository.findByFechaPrestamoBetween(fechaInicioFormateada, fechaFinalFormateada);
		
		PdfWriter writer = new PdfWriter(dest);
		PdfDocument pdfDoc = new PdfDocument(writer);
		
		pdfDoc.addNewPage();
		Document document = new Document(pdfDoc);
		
		crearCuerpo(document);
		crearTabla(document, prestamos);	
		document.close();
		
		byte[] pdfBytes = baos.toByteArray();
        return new ResponseEntity<>(pdfBytes, crearHeaders(pdfBytes), HttpStatus.OK);
	}
	
	private void crearCuerpo(Document document) {
		String content = "RESUMEN DE PRESTAMOS";
		Paragraph paragraph = new Paragraph(content);
		paragraph.setFontSize(14);
		paragraph.setTextAlignment(TextAlignment.CENTER);
		paragraph.setMargin(10);
		paragraph.setPaddingLeft(10);
		paragraph.setPaddingRight(10);
		paragraph.setWidth(1000);
		paragraph.setHeight(100);
		document.add(paragraph);
	}
	
	private void crearTabla(Document document, List<Prestamo> prestamos) {
		float [] pointColumnWidths = {150F, 150F, 150F, 150F, 150F, 150F};
		Table table = new Table(pointColumnWidths);

		table.addCell(new Cell().add(new Paragraph("Id")));
		table.addCell(new Cell().add(new Paragraph("Id Miembro")));
		table.addCell(new Cell().add(new Paragraph("Id Libro")));
		table.addCell(new Cell().add(new Paragraph("Fecha Prestamo")));
		table.addCell(new Cell().add(new Paragraph("Fecha Devolucion")));
		table.addCell(new Cell().add(new Paragraph("Estado")));
		
		for(Prestamo prestamo : prestamos) {
			table.addCell(new Cell().add(new Paragraph(prestamo.getId().toString())));
			table.addCell(new Cell().add(new Paragraph(prestamo.getMiembro().getId().toString())));
			table.addCell(new Cell().add(new Paragraph(prestamo.getLibro().getId().toString())));
			table.addCell(new Cell().add(new Paragraph(dateFormatter.transformarFechaNatural(prestamo.getFechaPrestamo().toString()))));
			table.addCell(new Cell().add(new Paragraph(dateFormatter.transformarFechaNatural(prestamo.getFechaDevolucion().toString()))));
			table.addCell(new Cell().add(new Paragraph(prestamo.getEstado().toString())));
		}
	}
		private HttpHeaders crearHeaders(byte[] pdfBytes) {
			HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_PDF);
	        headers.setContentDispositionFormData("attachment", "Resumen.pdf");
	        headers.setContentLength(pdfBytes.length);
	        return headers;
		}
	}
