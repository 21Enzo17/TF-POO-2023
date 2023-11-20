package ar.edu.unju.fi.tp9.service.imp;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.dto.PrestamoInfoDto;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.service.IResumenService;
import ar.edu.unju.fi.tp9.util.DateFormatter;
import ar.edu.unju.fi.tp9.util.HeaderGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service("excelService")
public class ExcelResumenServiceImp implements IResumenService{
    private  Logger logger = Logger.getLogger(this.getClass());

    @Autowired
	DateFormatter dateFormatter;
    @Autowired
    HeaderGenerator headerGenerator;
    
    /**
     * Genera un archivo excel con los prestamos realizados entre las fechas indicadas
     * @param prestamosDto
     * @param fechaInicio
     * @param fechaFin
     * @return ResponseEntity<byte[]> con el archivo excel
     * 
     */
    @Override
    public ResponseEntity<byte[]> realizarResumen(List<PrestamoInfoDto> prestamosDto, String fechaInicio, String fechaFin) throws ManagerException{
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    	
        logger.info("Generando excel");
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Prestamos");
        CreationHelper creationHelper = workbook.getCreationHelper();
        
        generarColumnas(sheet, workbook);
        copiarDatos(sheet, prestamosDto, creationHelper);
        ajustarAncho(sheet);
        agregarTitulo(sheet, fechaInicio, fechaFin);
        try{
            cerrarArchivo(workbook, outputStream);
        }catch(Exception e){
            throw new ManagerException("Hubo un error al cerrar el archivo, intentelo mas tarde");
        }
        
        logger.info("Excel generado correctamente");
        return new ResponseEntity<>(outputStream.toByteArray(), headerGenerator.crearHeadersExcel(), 200);
    }
    

    /**
     * Metodo encargado de agregar las fechas solicitadas en el documento
     * @param sheet
     * @param fechaIncio
     * @param fechaFin
     */
    private void agregarTitulo(Sheet sheet, String fechaIncio, String fechaFin) {
    	Row titulo = sheet.createRow(0);
    	Cell cell = titulo.createCell(0);
    	cell.setCellValue("Resumen de prestamos entre " + fechaIncio + " y " + fechaFin);
    }
    
    /**
     * Metodo encargado de generar las columnas del excel, con un formato.
     * @param sheet
     * @param workbook
     */
    private void generarColumnas(Sheet sheet, Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);


    	Row headerRow = sheet.createRow(1);
        String[] headers = {"id", "Miembro", "Libro", "Fecha del Prestamo", "Fecha de Devolucion", "Estado"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

    }
    
    /**
     * Metodo encargado de copiar los datos de los prestamos en el excel
     * @param sheet
     * @param prestamosDto
     * @param creationHelper
     */
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

    /**
     * Metodo encargado de cerrar el archivo excel
     * @param workbook
     * @param outputStream
     */
    private void cerrarArchivo(Workbook workbook, ByteArrayOutputStream outputStream) {
    	try {
    		workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            logger.error("Error al cerrar el archivo excel");
        }
    }
    

    /**
     * Metodo encargado de ajustar el ancho de las columnas del excel
     * @param sheet
     */
    private void ajustarAncho(Sheet sheet){
        sheet.setColumnWidth(0, 256*5);
        for (int i = 1; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
