package ar.edu.unju.fi.tp9.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unju.fi.tp9.dto.MiembroDto;
import ar.edu.unju.fi.tp9.service.IMiembroService;

@RestController
@RequestMapping("/api/v1/miembro")
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class MiembroResource {
    private final Logger logger =  Logger.getLogger(this.getClass());

    @Autowired
    IMiembroService miembroService;

    @PostMapping("/miembros")
    public ResponseEntity<?> guardarUnMiembro(@RequestBody MiembroDto miembroDto){
        logger.debug("Agregando Miembro: " + miembroDto.getCorreo());
        Map<String, Object> response = new HashMap<String, Object>();
        try{
            response.put("Objeto",miembroService.guardarMiembro(miembroDto));
            response.put("Mensaje", "Miembro guardado con exito");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
        }catch(Exception e){
            logger.error("Error al guardar el miembro: "+ e.getMessage());
            response.put("Mensaje", "Error al guardar miembro: " + e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/miembros/{id}")
    public ResponseEntity<?> obtenerMiembroPorId(@PathVariable Long id){
        Map<String, Object> response = new HashMap<String, Object>();
        try{
            MiembroDto miembro = miembroService.obtenerMiembroById(id);
            response.put("Miembro", miembro);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }catch(Exception e){
             logger.error("Error al buscar miembro: "+ e.getMessage());
            response.put("Mensaje", "Error 404, miembro no encontrado");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/miembros")
    public ResponseEntity<?> obtenerMiembroPorCorreo(@RequestParam String correo){
        Map<String, Object> response = new HashMap<String, Object>();
        try{
            MiembroDto miembro = miembroService.obtenerMiembroByCorreo(correo);
            response.put("Miembro", miembro);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }catch(Exception e){
            logger.error("Error al buscar miembro: "+ e.getMessage());
            response.put("Mensaje", "Error 404, miembro no encontrado");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/miembros/")
    public ResponseEntity<?> modificarMiembro(@RequestBody MiembroDto miembroDto){
        Map<String, Object> response = new HashMap<String, Object>();
        try{
            response.put("Objeto",miembroService.modificarMiembro(miembroDto));
            response.put("Mensaje", "Miembro modificado con exito");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }catch(Exception e){
            logger.error("Error al modificar miembro: "+ e.getMessage());
            response.put("Mensaje", "Error al modificar miembro: " + e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/miembros/{id}")
    public ResponseEntity<?> eliminarMiembro(@PathVariable Long id){
        Map<String, Object> response = new HashMap<String, Object>();
        try{
            miembroService.eliminarMiembroPorId(id);
            response.put("Mensaje", "Miembro eliminado con exito");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }catch(Exception e){
            logger.error("Error al eliminar miembro: "+ e.getMessage());
            response.put("Mensaje", "Error al eliminar miembro: " + e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
