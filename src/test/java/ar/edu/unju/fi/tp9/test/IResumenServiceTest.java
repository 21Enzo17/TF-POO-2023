package ar.edu.unju.fi.tp9.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hpsf.Array;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import ar.edu.unju.fi.tp9.dto.PrestamoInfoDto;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.service.IPrestamoService;
import ar.edu.unju.fi.tp9.service.IResumenService;
import ar.edu.unju.fi.tp9.util.DateFormatter;


@SpringBootTest
public class IResumenServiceTest {
    
    @Autowired
    @Qualifier("excelService")
    IResumenService excelTarget;

    @Autowired
    @Qualifier("pdfService")
    IResumenService pdfTarget;

    @Autowired
    IPrestamoService prestamoService;

    @Autowired
    DateFormatter dateFormatter;

    static List<PrestamoInfoDto> prestamosDto;
    static String fechaInicio;
    static String fechaFin;
    

    @BeforeEach
    void setUp() throws Exception {
        fechaInicio = "10/05/2020 - 00:12";
        fechaFin = "20/11/2023 - 00:12";
        prestamosDto = new ArrayList<>();
    }

    @AfterEach
    void tearDown() throws Exception {
        fechaInicio = null;
        fechaFin = null;
        prestamosDto = null;
    }

    @Test
    void crearExcel() throws ManagerException{
        ResponseEntity<byte[]> response = excelTarget.realizarResumen(prestamosDto, fechaInicio, fechaFin);
        assertNotNull(response);
    }

    @Test
    void crearPdf() throws ManagerException{
        ResponseEntity<byte[]> response = pdfTarget.realizarResumen(prestamosDto, fechaInicio, fechaFin);
        assertNotNull(response);
    }

}
