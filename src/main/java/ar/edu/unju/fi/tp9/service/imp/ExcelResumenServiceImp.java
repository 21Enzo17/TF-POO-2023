package ar.edu.unju.fi.tp9.service.imp;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.entity.Prestamo;
import ar.edu.unju.fi.tp9.repository.PrestamoRepository;
import ar.edu.unju.fi.tp9.service.IResumenService;
import ar.edu.unju.fi.tp9.util.DateFormatter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Service("excelService")
public class ExcelResumenServiceImp implements IResumenService{
    @Autowired
    private PrestamoRepository prestamoRepository; 
    
    @Autowired
	DateFormatter dateFormatter;
    
    @Override
    public ResponseEntity<byte[]> realizarResumen(String fechaInicio, String fechaFinal) {
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    	
		LocalDateTime fechaInicioFormateada = dateFormatter.fechDateTime(fechaInicio);
		LocalDateTime fechaFinalFormateada = dateFormatter.fechDateTime(fechaFinal);
		
		List<Prestamo> prestamos = prestamoRepository.findByFechaPrestamoBetween(fechaInicioFormateada, fechaFinalFormateada);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Prestamos");

        generarColumnas(sheet);
        copiarDatos(sheet, prestamos);
        cerrarArchivo(workbook, outputStream);
        
        return new ResponseEntity<>(outputStream.toByteArray(), crearHeaders(), 200);
    }
    
    private void generarColumnas(Sheet sheet) {
    	Row headerRow = sheet.createRow(0);
        String[] headers = {"id", "id del Miembro", "id del Libro", "Fecha del Prestamo", "Fecha de Devolucion", "Estado"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }
    
    private void copiarDatos(Sheet sheet, List<Prestamo> prestamos) {
    	int numeroDeFila = 1;
        for (Prestamo prestamo : prestamos) {
            Row fila = sheet.createRow(numeroDeFila++);
            fila.createCell(0).setCellValue(prestamo.getId());
            fila.createCell(1).setCellValue(prestamo.getMiembro().getId());
            fila.createCell(2).setCellValue(prestamo.getLibro().getId());
            fila.createCell(3).setCellValue(prestamo.getFechaPrestamo());
            fila.createCell(4).setCellValue(prestamo.getFechaDevolucion());
            fila.createCell(5).setCellValue(prestamo.getEstado().toString());
        }
    }
    
    
    private void cerrarArchivo(Workbook workbook, ByteArrayOutputStream outputStream) {
    	try {
    		workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private HttpHeaders crearHeaders(){
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Prestamos.xlsx");
        return headers;
    }
}
