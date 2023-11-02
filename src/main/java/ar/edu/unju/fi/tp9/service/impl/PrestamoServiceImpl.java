package ar.edu.unju.fi.tp9.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.dto.LibroGuardarDto;
import ar.edu.unju.fi.tp9.dto.PrestamoDto;
import ar.edu.unju.fi.tp9.entity.Libro;
import ar.edu.unju.fi.tp9.entity.Prestamo;
import ar.edu.unju.fi.tp9.entity.util.EstadoPrestamo;
import ar.edu.unju.fi.tp9.repository.PrestamoRepository;
import ar.edu.unju.fi.tp9.service.PrestamoService;
import ar.edu.unju.fi.tp9.util.EstadoLibro;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PrestamoServiceImpl implements PrestamoService{
	@Autowired
	PrestamoRepository prestamoRepository;
	
	private static Logger logger = Logger.getLogger(PrestamoServiceImpl.class);
	ModelMapper mapper = new ModelMapper();
	
	private static DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	
	private void transferirDatosAPrestamo(Prestamo prestamo, PrestamoDto prestamoDto) {
		prestamo.setId(prestamo.getId());
		prestamo.setEstado(prestamoDto.getEstado());
		mapper.map(prestamoDto.getLibro(), prestamo.getLibro());
		prestamo.setFechaPrestamo(LocalDateTime.parse(prestamoDto.getFechaPrestamo(), formatoFecha));
		prestamo.setFechaDevolucion(LocalDateTime.parse(prestamoDto.getFechaDevolucion(), formatoFecha));
	}
	
	private void transferirDatosADto(Prestamo prestamo, PrestamoDto prestamoDto) {
		prestamoDto.setId(prestamo.getId());
		prestamoDto.setEstado(prestamo.getEstado());
		mapper.map(prestamo.getLibro(), prestamoDto.getLibro());
		prestamoDto.setFechaPrestamo(prestamo.getFechaPrestamo().toString());
		prestamoDto.setFechaDevolucion(prestamo.getFechaDevolucion().toString());
	}

	//FIXME error de persist con libro.
	@Override
	public void guardarPrestamo(PrestamoDto prestamoDto) {
		Prestamo prestamoGuardar = new Prestamo();
		
		prestamoDto.setEstado(EstadoPrestamo.PRESTADO.toString());
		transferirDatosAPrestamo(prestamoGuardar, prestamoDto);
		
		logger.info("Prestamo del miembro ... registrado.");
		prestamoRepository.save(prestamoGuardar);
	}

	@Override
	public void devolverPrestamo(PrestamoDto prestamoDto) {
		Prestamo prestamoDevolver = new Prestamo();
		
		if(prestamoRepository.existsById(prestamoDto.getId())) {
			prestamoDto.setEstado(EstadoPrestamo.DEVUELTO.toString());
			transferirDatosAPrestamo(prestamoDevolver, prestamoDto);
			
			logger.info("Prestamo de miembro ... devuelto");
			prestamoRepository.save(prestamoDevolver);
		}
		else {
			logger.error("Prestamo con id: " + prestamoDto.getId() + " no ah sido registrado.");
			throw new EntityNotFoundException("Prestamo con id: " + prestamoDto.getId() + " no ah sido registrado.");
		}
	}

	@Override
	public PrestamoDto buscarPrestamoPorId(Long id) {
		Optional<Prestamo> prestamoBuscado = prestamoRepository.findById(id);
		PrestamoDto prestamoDto = new PrestamoDto();
		
		if(prestamoBuscado.isEmpty()) 
			return null;
		else {
			transferirDatosADto(prestamoBuscado.get(), prestamoDto);
			return prestamoDto;
		}
	}
	
	public long getPrestamosSize() {
		return prestamoRepository.count();
	}

	@Override
	public void eliminarPrestamo(PrestamoDto prestamoDto) {
		Prestamo prestamoEliminar = new Prestamo();
		transferirDatosAPrestamo(prestamoEliminar, prestamoDto);
		
		prestamoRepository.delete(prestamoEliminar);
	}
	
}
