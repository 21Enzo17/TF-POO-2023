package ar.edu.unju.fi.tp9.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unju.fi.tp9.dto.PrestamoDto;
import ar.edu.unju.fi.tp9.service.IPrestamoService;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/prestamo")
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class PrestamoResource {
    private final Logger logger =  Logger.getLogger(this.getClass());

    @Autowired
    IPrestamoService prestamoService;

    @PostMapping("/prestamos")
    public ResponseEntity<?> guardarPrestmo(@RequestBody PrestamoDto prestamoDto){
        logger.debug("Agregando Prestamo");
        Map<String, Object> response = new HashMap<String, Object>();
        try{
            prestamoService.guardarPrestamo(prestamoDto);
            response.put("Mensaje", "Prestamo guardado con exito");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
        }catch(Exception e){
            logger.error("Error al guardar el prestamo: "+ e.getMessage());
            response.put("Mensaje", "Error al guardar prestamo: " + e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/prestamos/{id}")
    public ResponseEntity<?> obtenerPrestamo(@PathVariable Integer id){
        Map<String, Object> response = new HashMap<String, Object>();
        try{
            PrestamoDto prestamo = prestamoService.obtenerPrestamoById(id);
            response.put("Prestamo", prestamo);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }catch(Exception e){
             logger.error("Error al buscar prestamo: "+ e.getMessage());
            response.put("Mensaje", "Error 404, prestamo no encontrado");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/prestamos/{id}")
    public ResponseEntity<?> devolverPrestamo(@PathVariable Integer id){
        Map<String, Object> response = new HashMap<String, Object>();
        try{
            prestamoService.devolucionPrestamo(id);
            response.put("Mensaje", "Prestamo devuelto con exito");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }catch(Exception e){
             logger.error("Error al devolver prestamo: "+ e.getMessage());
            response.put("Mensaje", "Error 404, prestamo no encontrado");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
    }
}