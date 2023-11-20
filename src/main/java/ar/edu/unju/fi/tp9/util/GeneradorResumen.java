package ar.edu.unju.fi.tp9.util;

import java.io.FileNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import ar.edu.unju.fi.tp9.service.IResumenPrestamos;

@Component
public class GeneradorResumen {
	private IResumenPrestamos resumen;
	
	private String fechaInicio;
	
	private String fechaFin;
	
	public GeneradorResumen(IResumenPrestamos resumen, String fechaInicio, String fechaFin) {
		this.resumen = resumen;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}
	
	public ResponseEntity<byte[]> generarResumen() throws FileNotFoundException {
		return resumen.realizarResumen(fechaInicio, fechaFin);
	}
}
