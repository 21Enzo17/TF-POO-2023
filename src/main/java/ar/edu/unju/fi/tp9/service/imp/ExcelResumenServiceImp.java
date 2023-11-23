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
    	
        logger.info("Generando excel con los prestamos realizados entre " + fechaInicio + " y " + fechaFin + "...");
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Prestamos");
        CreationHelper creationHelper = workbook.getCreationHelper();

        CellStyle estilo = generarEstilo(workbook);
        
        generarEncabezados(sheet, workbook);
        copiarDatos(sheet, prestamosDto, creationHelper,estilo);
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
    	Cell cell = titulo.createCell(1);
    	cell.setCellValue("Resumen de prestamos entre " + fechaIncio + " y " + fechaFin);
    }
    
    /**
     * Metodo encargado de generar los Encabezados del excel, con un formato.
     * @param sheet
     * @param workbook
     */
    private void generarEncabezados(Sheet sheet, Workbook workbook) {
        CellStyle encabezado = generarEstilo(workbook);
        Font font = workbook.createFont();
        font.setBold(true);
        encabezado.setFont(font);
    
        Row headerRow = sheet.createRow(2);
        String[] headers = {"", "Id", "Miembro", "Libro", "Fecha del Prestamo", "Estado", ""};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            if(i != 0 && i != headers.length - 1) {
                cell.setCellStyle(encabezado);
            }
        }
    }
    
    /**
     * Metodo encargado de copiar los datos de los prestamos en el excel
     * @param sheet
     * @param prestamosDto
     * @param creationHelper
     * @param estilo
     */
    private void copiarDatos(Sheet sheet, List<PrestamoInfoDto> prestamosDto, CreationHelper createHelper,CellStyle estilo) {
        int numeroFila = 3;
        for (PrestamoInfoDto prestamo : prestamosDto) {
            Row fila = sheet.createRow(numeroFila++);
            for(int i = 0; i < 6; i++) { 
                Cell celda = fila.createCell(i);
                if(i != 0) {
                    switch(i) {
                        case 1:
                            celda.setCellValue(prestamo.getId());
                            break;
                        case 2:
                            celda.setCellValue(prestamo.getNombreMiembro());
                            break;
                        case 3:
                            celda.setCellValue(prestamo.getTituloLibro());
                            break;
                        case 4:
                            celda.setCellValue(prestamo.getFechaPrestamo());
                            break;
                        case 5:
                            celda.setCellValue(prestamo.getEstado());
                            break;
                    }
                    celda.setCellStyle(estilo);
                }
            }
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
        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
            int ancho = sheet.getColumnWidth(i);
            if (i == 5) {  // Si es la columna F
                ancho = Math.max(ancho, 256*10);  // Asegura que el ancho sea al menos el de 10 caracteres
            }
            sheet.setColumnWidth(i, ancho + 256);  // Agrega 1 carácter de espacio adicional
        }
    }

    /**
     * Metodo encargado de generar el estilo de las celdas del excel
     * @param workbook
     * @return CellStyle
     */
    private CellStyle generarEstilo(Workbook workbook){
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}
