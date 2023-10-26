package ar.edu.unju.fi.tp9.service;

import ar.edu.unju.fi.tp9.entity.Prestamo;

public interface PrestamoService {
	
	void guardarPrestamo(Prestamo prestamo);
	
	void editarPrestamo(Prestamo prestamo);
	
	void eliminarPrestamo(Long id);
	
	Prestamo buscarPrestamoPorId(Long id);
}
