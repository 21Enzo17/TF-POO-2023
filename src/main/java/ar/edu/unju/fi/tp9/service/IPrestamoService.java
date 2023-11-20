package ar.edu.unju.fi.tp9.service;

<<<<<<< src/main/java/ar/edu/unju/fi/tp9/service/IPrestamoService.java
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;


import ar.edu.unju.fi.tp9.dto.MiembroDto;

import java.io.FileNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import ar.edu.unju.fi.tp9.dto.PrestamoDto;
import ar.edu.unju.fi.tp9.dto.PrestamoInfoDto;
import ar.edu.unju.fi.tp9.exception.ManagerException;

@Service
public interface IPrestamoService {

    public PrestamoInfoDto guardarPrestamo(Long idMiembro, Long idLibro) throws ManagerException;

    public ByteArrayOutputStream generarComprobante(Long idPrestamo) throws ManagerException;

    public void devolucionPrestamo(Long id) throws ManagerException;

    public PrestamoDto obtenerPrestamoById(Long id) throws ManagerException;

    public void eliminarPrestamoById(Long id) throws ManagerException;

    public ResponseEntity<byte[]> realizarResumenExcel(String fechaInicio, String fechaFin) throws FileNotFoundException;
    
    public ResponseEntity<byte[]> realizarResumenPdf(String fechaInicio, String fechaFin) throws FileNotFoundException;
}
