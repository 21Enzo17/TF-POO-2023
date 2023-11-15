package ar.edu.unju.fi.tp9.service;

import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.dto.MiembroDto;
import ar.edu.unju.fi.tp9.entity.Miembro;
import ar.edu.unju.fi.tp9.exception.ManagerException;


@Service
public interface IMiembroService {
    /**
     * Metodo encargado de guardar un miembro 
     * @param miembro
     */
    public MiembroDto guardarMiembro(MiembroDto miembro) throws ManagerException;

    /**
     * Metodo encargado de eliminar un miembro
     * @param id
     */
    public void eliminarMiembroPorCorreo(String correo) throws ManagerException;

    public void eliminarMiembroPorId(Integer id) throws ManagerException;

    public MiembroDto modificarMiembro(MiembroDto miembro) throws ManagerException;
    
    public MiembroDto obtenerMiembroByCorreo(String correo) throws ManagerException;

    public MiembroDto obtenerMiembroById(Integer id) throws ManagerException;

    public Miembro miembroDtoAMiembro(MiembroDto miembroDto);

    public MiembroDto miembroAMiembroDto(Miembro miembro);
}
