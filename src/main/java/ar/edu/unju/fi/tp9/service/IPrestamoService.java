package ar.edu.unju.fi.tp9.service;

import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.dto.MiembroDto;
import ar.edu.unju.fi.tp9.dto.PrestamoDto;
import ar.edu.unju.fi.tp9.dto.PrestamoInfoDto;
import ar.edu.unju.fi.tp9.exception.ManagerException;

@Service
public interface IPrestamoService {

    public PrestamoInfoDto guardarPrestamo(Long idMiembro, Long idLibro) throws ManagerException;

    public void devolucionPrestamo(Long id) throws ManagerException;

    public PrestamoDto obtenerPrestamoById(Long id) throws ManagerException;

    public void eliminarPrestamoById(Long id) throws ManagerException;

}
