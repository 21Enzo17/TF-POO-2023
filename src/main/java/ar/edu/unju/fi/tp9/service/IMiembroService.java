package ar.edu.unju.fi.tp9.service;

import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.dto.MiembroDto;


@Service
public interface IMiembroService {
    /**
     * Metodo encargado de guardar o editar un miembro
     * @param miembro
     */
    public void guardarMiembro(MiembroDto miembro);

    /**
     * Metodo encargado de eliminar un miembro
     * @param id
     */
    public void eliminarMiembro(MiembroDto miembro);

    public void modificarMiembro(MiembroDto miembro);
    
    public MiembroDto obtenerMiembroByCorreo(String correo);

    public MiembroDto obtenerMiembroById(Integer id);
}
