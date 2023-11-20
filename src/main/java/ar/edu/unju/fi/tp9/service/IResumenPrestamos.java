package ar.edu.unju.fi.tp9.service;

import java.io.FileNotFoundException;

import org.springframework.http.ResponseEntity;

public interface IResumenPrestamos {
	public ResponseEntity<byte[]> realizarResumen(String fechaInicio, String fechaFinal) throws FileNotFoundException;
}
