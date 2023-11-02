package ar.edu.unju.fi.tp9.service;

import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.dto.MiembroDto;
import ar.edu.unju.fi.tp9.dto.PrestamoDto;

@Service
public interface IPrestamoService {

    public void guardarPrestamo(PrestamoDto prestamo);

    public PrestamoDto buscarPrestamoPorMiembro(MiembroDto miembroDto);

    public void devolucionPrestamo(PrestamoDto prestamoDto);

}
