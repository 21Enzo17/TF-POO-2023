package ar.edu.unju.fi.tp9.service.imp;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.apache.log4j.Logger;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import ar.edu.unju.fi.tp9.dto.PrestamoInfoDto;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.service.IResumenService;
import ar.edu.unju.fi.tp9.util.DateFormatter;
import ar.edu.unju.fi.tp9.util.HeaderGenerator;

@Service("pdfService")
public class PdfResumenServiceImp implements IResumenService {
    Logger logger =  Logger.getLogger(this.getClass());
	
	@Autowired
	DateFormatter dateFormatter;
	@Autowired
	HeaderGenerator headerGenerator;
	

	/**
	 * Genera un archivo pdf con los prestamos realizados entre las fechas indicadas
	 * @param prestamosDto
	 * @param fechaInicio
	 * @param fechaFin
	 * @return ResponseEntity<byte[]> con el archivo pdf
	 * @throws ManagerException
	 */
	@Override
	public ResponseEntity<byte[]> realizarResumen(List<PrestamoInfoDto> prestamosDto, String fechaInicio, String fechaFin) throws ManagerException {
		ByteArrayOutputStream resumen = new ByteArrayOutputStream();
		logger.info("Generando pdf con los prestamos realizados entre " + fechaInicio + " y " + fechaFin + "...");
		PdfWriter writer = new PdfWriter(resumen);
		PdfDocument pdfDoc = new PdfDocument(writer);
		
		pdfDoc.addNewPage();
		Document document = new Document(pdfDoc);
		
		agregarImagen(document);
		crearCuerpo(document, fechaInicio, fechaFin);
		crearTabla(document, prestamosDto);	
		document.close();
		
		byte[] pdfBytes = resumen.toByteArray();
		logger.info("Pdf generado correctamente");
        return new ResponseEntity<>(pdfBytes, headerGenerator.crearHeadersPdf(pdfBytes), HttpStatus.OK);
	}
	
	
	/**
	 * Metodo encargado de agregar el logo d la biblioteca
	 * @param document
	 * @throws ManagerException
	 */
	private void agregarImagen(Document document) throws ManagerException{
		String path = "src/main/resources/images/Bibliowlteca.png";
		ImageData data;
		
		try {
			data = ImageDataFactory.create(path);
			Image image = new Image(data);
			image.setPadding(50);
			image.setWidth(100);
			image.setMaxHeight(150);
			image.setAutoScale(false);
			
			Paragraph paragraph = new Paragraph();
	        paragraph.add(image);
	        paragraph.setTextAlignment(TextAlignment.CENTER);
	        paragraph.setHorizontalAlignment(HorizontalAlignment.CENTER);
			paragraph.setMarginBottom(5);
	        
	        document.add(paragraph);
		} catch (MalformedURLException e) {
			logger.error("Error al agregar imagen");
			throw new ManagerException("Hubo un error al agregar la imagen");
		}
	}
	

	/**
	 * Metodo encargado de crear el cuerpo del pdf
	 * @param document
	 * @param fechaInicio
	 * @param fechaFin
	 */
	private void crearCuerpo(Document document, String fechaInicio, String fechaFin) {
		String content = "RESUMEN DE PRESTAMOS\nEntre " + fechaInicio + " y " + fechaFin;
		Paragraph paragraph = new Paragraph(content);
		paragraph.setFontSize(20);
		paragraph.setTextAlignment(TextAlignment.CENTER);
		paragraph.setMarginBottom(0);
		paragraph.setPaddingLeft(10);
		paragraph.setPaddingRight(10);
		paragraph.setWidth(1000);
		paragraph.setHeight(100);
		document.add(paragraph);
	}
	

	/**
	 * Metodo encargado de crear la tabla con los prestamos
	 * @param document
	 * @param prestamosDto
	 */
	private void crearTabla(Document document, List<PrestamoInfoDto> prestamosDto) {
		float [] pointColumnWidths = {50F, 150F, 225F, 225F, 150F};
		Table table = new Table(pointColumnWidths);

		Style centradoNegritas = new Style().setTextAlignment(TextAlignment.CENTER).setBold();
		Style centrado = new Style().setTextAlignment(TextAlignment.CENTER);

		table.addCell(new Cell().add(new Paragraph("Id")).addStyle(centradoNegritas));
		table.addCell(new Cell().add(new Paragraph("Miembro")).addStyle(centradoNegritas));
		table.addCell(new Cell().add(new Paragraph("Libro")).addStyle(centradoNegritas));
		table.addCell(new Cell().add(new Paragraph("Fecha Prestamo")).addStyle(centradoNegritas));
		table.addCell(new Cell().add(new Paragraph("Estado")).addStyle(centradoNegritas));
		
		for(PrestamoInfoDto dto : prestamosDto) {
			table.addCell(new Cell().add(new Paragraph(dto.getId().toString())).addStyle(centradoNegritas));
			table.addCell(new Cell().add(new Paragraph(dto.getNombreMiembro())).addStyle(centrado));
			table.addCell(new Cell().add(new Paragraph(dto.getTituloLibro())).addStyle(centrado));
			table.addCell(new Cell().add(new Paragraph(dto.getFechaPrestamo())).addStyle(centrado));
			table.addCell(new Cell().add(new Paragraph(dto.getEstado())).addStyle(centrado));
		}
        document.add(table);
	}


}
