package ar.edu.unju.fi.tp9.service.impl;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.entity.Prestamo;
import ar.edu.unju.fi.tp9.repository.PrestamoRepository;
import ar.edu.unju.fi.tp9.service.PrestamoService;
import jakarta.persistence.EntityNotFoundException;

//FIXME Aplicar DTO.
@Service
public class PrestamoServiceImpl implements PrestamoService{

	@Autowired
	PrestamoRepository prestamoRepository;
	
	private static Logger logger = Logger.getLogger(PrestamoServiceImpl.class);
	
	/**
	 * guarda prestamo pasado por parametro.
	 */
	@Override
	public void guardarPrestamo(Prestamo prestamo) {
		logger.info("Guardando prestamo...");
		prestamoRepository.save(prestamo);
	}

	/**
	 * edita un prestamo pasado por parametro, si el id no esta registrado devuelve error.
	 */
	@Override
	public void editarPrestamo(Prestamo prestamo) {
		if( !prestamoRepository.existsById(prestamo.getId()) ) {
			logger.error("Prestamo con id: " + prestamo.getId() + " no registrado.");
			throw new EntityNotFoundException("Prestamo con id: " + prestamo.getId() + " no registrado.");
		}
		else {
			logger.info("Prestamo con id: " + prestamo.getId() + "modificado.");
			prestamoRepository.save(prestamo);
		}
	}

	/**
	 * elimina un prestamo buscado por id, sino devuelve error.
	 */
	@Override
	public void eliminarPrestamo(Long id) {
		Optional<Prestamo> prestamoEliminar = prestamoRepository.findById(id);
		
		if(prestamoEliminar.isEmpty()) {
			logger.error("Prestamo con id: " + id + " no esta registrado para ningun prestamo.");
			throw new EntityNotFoundException("La id: " + id + "ingresada no esta registrada para ningun prestamo.");
		}
		else {
			prestamoRepository.delete(prestamoEliminar.get());
			logger.info("Prestamo con id: " + id + " eliminado correctamente.");
		}
	}

	/**
	 * Devuelve prestamo buscado por id, si no duelve null.
	 */
	@Override
	public Prestamo buscarPrestamoPorId(Long id) {
		Optional<Prestamo> prestamoBuscado = prestamoRepository.findById(id);
		
		if(prestamoBuscado.isEmpty())
			return null;
		else
			return prestamoBuscado.get();
	}

}
