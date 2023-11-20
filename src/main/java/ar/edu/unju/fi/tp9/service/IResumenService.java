package ar.edu.unju.fi.tp9.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.dto.PrestamoInfoDto;

@Service
public interface IResumenService {
    public ResponseEntity<byte[]> realizarResumen(List<PrestamoInfoDto> prestamosDto, String fechaInicio, String fechaFin);
    
} 