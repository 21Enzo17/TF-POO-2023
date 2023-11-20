package ar.edu.unju.fi.tp9.service.imp;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.dto.PrestamoInfoDto;
import ar.edu.unju.fi.tp9.service.IResumenService;
import ar.edu.unju.fi.tp9.util.DateFormatter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service("excelService")
public class ExcelResumenServiceImp implements IResumenService{ 
    @Autowired
	DateFormatter dateFormatter;
    
    @Override
    public ResponseEntity<byte[]> realizarResumen(List<PrestamoInfoDto> prestamosDto, String fechaInicio, String fechaFin) {
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    	
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Prestamos");
        CreationHelper creationHelper = workbook.getCreationHelper();
        
        agregarTitulo(sheet, fechaInicio, fechaFin);
        generarColumnas(sheet);
        copiarDatos(sheet, prestamosDto, creationHelper);
        cerrarArchivo(workbook, outputStream);
        
        return new ResponseEntity<>(outputStream.toByteArray(), crearHeaders(), 200);
    }
    
    private void agregarTitulo(Sheet sheet, String fechaIncio, String fechaFin) {
    	Row titulo = sheet.createRow(0);
    	Cell cell = titulo.createCell(0);
    	cell.setCellValue("Resumen de prestamos entre " + fechaIncio + " y " + fechaFin);
    }
    
    private void generarColumnas(Sheet sheet) {
    	Row headerRow = sheet.createRow(1);
        String[] headers = {"id", "Miembro", "Libro", "Fecha del Prestamo", "Fecha de Devolucion", "Estado"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }
    
    private void copiarDatos(Sheet sheet, List<PrestamoInfoDto> prestamosDto, CreationHelper creationHelper) {
    	int numeroDeFila = 2;
    	
        for (PrestamoInfoDto prestamo : prestamosDto) {
            Row fila = sheet.createRow(numeroDeFila++);
            fila.createCell(0).setCellValue(prestamo.getId());
            fila.createCell(1).setCellValue(prestamo.getNombreMiembro());
            fila.createCell(2).setCellValue(prestamo.getTituloLibro());
            
            Cell fechaPrestamoCell = fila.createCell(3);
            fechaPrestamoCell.setCellValue(prestamo.getFechaPrestamo());
            fechaPrestamoCell.getCellStyle().setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm"));
            
            Cell fechaDevolucionCell = fila.createCell(4);
            fechaDevolucionCell.setCellValue(prestamo.getFechaDevolucion());
            fechaDevolucionCell.getCellStyle().setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm"));
            
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
