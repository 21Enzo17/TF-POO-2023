package ar.edu.unju.fi.tp9.service;

import ar.edu.unju.fi.tp9.dto.PrestamoDto;

public interface PrestamoService {
	
	void guardarPrestamo(PrestamoDto prestamoDto);
	
	void devolverPrestamo(PrestamoDto prestamoDto);
	
	PrestamoDto buscarPrestamoPorId(Long id);
	
	public long getPrestamosSize();
	
	public void eliminarPrestamo(PrestamoDto prestamoDto);
}
