package ar.edu.unju.fi.tp9.service.imp;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.dto.AlumnoDto;
import ar.edu.unju.fi.tp9.dto.DocenteDto;
import ar.edu.unju.fi.tp9.dto.MiembroDto;
import ar.edu.unju.fi.tp9.entity.Alumno;
import ar.edu.unju.fi.tp9.entity.Docente;
import ar.edu.unju.fi.tp9.entity.Miembro;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.repository.MiembroRepository;
import ar.edu.unju.fi.tp9.service.IMiembroService;

@Service
public class MiembroServiceImp implements IMiembroService {
    static Logger logger = Logger.getLogger(MiembroServiceImp.class);
    
    @Autowired
    MiembroRepository miembroRepository;

    /**
     * Este metodo guarda un miembro en la base de datos, comprobando antes que no se repita el correo.
     * @param miembroDto
     */
    @Override
    public void guardarMiembro(MiembroDto miembro) throws ManagerException {
        if(miembroRepository.findByCorreo(miembro.getCorreo()).orElse(null) != null){
            throw new ManagerException("Error al guardar alumno, correo repetido");
        }else{
            miembroRepository.save(miembroDtoAMiembro(miembro));
            logger.debug("Miembro: " + miembro.getNombre() + " guardado con exito");
        }
        
    }

    /**
     * Este metodo elimina un miembro de la base de datos
     * @param miembroDto
     */
    @Override
    public void eliminarMiembroPorCorreo(String correo) throws ManagerException{
        logger.debug("Buscado miembro con correo: " + correo);
        Miembro miembroBuscado = miembroRepository.findByCorreo(correo).orElse(null);
        if (miembroBuscado != null) {
            miembroRepository.delete(miembroBuscado);
            logger.debug(miembroBuscado.getNombre() + " eliminado con exito");
        } else {
            throw new ManagerException("No se encontró ningún miembro con el correo especificado");
        }
        
    }

    /**
     * Este metodo obtiene un miembro de la base de datos por su correo
     * @param correo
     * @return miembroDto
     */
    @Override
    public MiembroDto obtenerMiembroByCorreo(String correo) throws ManagerException {
        Miembro miembro;
        miembro = miembroRepository.findByCorreo(correo).orElse(null);
        if(miembro == null){
            throw new ManagerException("No existe el miembro");
        }
        return miembroAMiembroDto(miembro);
    }

    /**
     * Este metodo transforma cualquier tipo de miembroDto a un miembro
     * @param miembroDto
     * @return miembro
     */
    @Override
    public Miembro miembroDtoAMiembro(MiembroDto miembroDto){
        Miembro miembro;
        ModelMapper modelMapper = new ModelMapper();
        if(miembroDto.isAlumno()){
            miembro = modelMapper.map(miembroDto, Alumno.class);   
        }else{
            miembro = modelMapper.map(miembroDto, Docente.class);
        }
        return miembro;
    }

    /**
     * Este metodo transforma cualquier tipo de miembro a un miembroDto
     * @param miembro
     * @return
     */
    @Override
    public MiembroDto miembroAMiembroDto(Miembro miembro){
        MiembroDto miembroDto;
        ModelMapper modelMapper = new ModelMapper();
        if(miembro.isAlumno()){
            miembroDto = modelMapper.map(miembro, AlumnoDto.class);   
        }else{
            miembroDto = modelMapper.map(miembro, DocenteDto.class);
        }
        return miembroDto;
    }

    /**
     * En este metodo de modificacion, se presupone que el dto llega con el id del usuario a modificar, ya que se permite
     * modificar todos los atributos del miembro, ademas se checkea que el correo no este repetido
     * @param miembro
     * @return
     * @throws ManagerException
     */
    @Override
    public void modificarMiembro(MiembroDto miembro) throws ManagerException {
        if(!miembroRepository.existsById(miembro.getId())){
            throw new ManagerException("Error al modificar miembro, miembro no encontrado");
        }else{
            Miembro miembroEncontrado = miembroRepository.findByCorreo(miembro.getCorreo()).orElse(null);
            if(miembroEncontrado != null && !miembroEncontrado.getId().equals(miembro.getId()) ){
                logger.error("Nose pudo guardar con exito");
                throw new ManagerException("Error al modificar miembro, correo repetido");
            }else{
                miembroRepository.save(miembroDtoAMiembro(miembro));
                logger.debug("Miembro: " + miembro.getNombre() + " modificado con exito");
            }

        }
    }
    
    /**
     * Este metodo obtiene un miembro de la base de datos por su id
     * @param id
     * @return miembroDto
     * @throws ManagerException
     */
    @Override
    public MiembroDto obtenerMiembroById(Integer id) {
        Miembro miembro;
        miembro = miembroRepository.findById(id).orElse(null);
        
        return miembroAMiembroDto(miembro);
    }
}
