package ar.edu.unju.fi.tp9.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface IResumenService {
    public ResponseEntity<byte[]> realizarResumen(String fechaInicio, String fechaFinal);
    
} 