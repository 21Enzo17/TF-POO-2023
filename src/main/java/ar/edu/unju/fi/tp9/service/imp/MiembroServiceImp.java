package ar.edu.unju.fi.tp9.service.imp;


import java.time.LocalDateTime;
import java.util.Optional;

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
import ar.edu.unju.fi.tp9.util.DateFormatter;

@Service
public class MiembroServiceImp implements IMiembroService {
    static Logger logger = Logger.getLogger(MiembroServiceImp.class);
    
    @Autowired
    MiembroRepository miembroRepository;
    @Autowired
    DateFormatter dateFormatter;

    /**
     * Este metodo guarda un miembro en la base de datos, comprobando antes que no se repita el correo.
     * @param miembroDto
     */
    @Override
    public MiembroDto guardarMiembro(MiembroDto miembro) throws ManagerException {
        Miembro miembroReturn;
        if(miembroRepository.findByCorreo(miembro.getCorreo()).orElse(null) != null){
            throw new ManagerException("Error al guardar alumno, correo repetido");
        }else{
            miembroReturn = miembroRepository.save(crearUnMiembro(miembroDtoAMiembro(miembro)));
            logger.debug("Miembro: " + miembro.getNombre() + " guardado con exito");
        }
        return miembroAMiembroDto(miembroReturn);
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
        logger.debug(miembro.getNumeroMiembro() + " encontrado con exito");
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
        // Se mappea de acuerdo al tipo de miembro
        if(miembroDto.isAlumno()){
            miembro = modelMapper.map(miembroDto, Alumno.class);         
        }else{
            miembro = modelMapper.map(miembroDto, Docente.class);
        }
        // Asigno fecha de bloqueo actual o uso la existente
        if(miembroDto.getFechaBloqueo() != null){
            miembro.setFechaBloqueo(dateFormatter.fechDateTime(miembroDto.getFechaBloqueo()));
        }
        logger.debug("Datos del miembro: " + miembro.getCorreo() + " mapeados con exito");
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
        miembroDto.setFechaBloqueo(dateFormatter.transformarFechaNatural(miembro.getFechaBloqueo().toString()));
        logger.debug("Datos del miembro: " + miembro.getCorreo() + " mapeados con exito");
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
    public MiembroDto modificarMiembro(MiembroDto miembro) throws ManagerException {
        Miembro retorno;
        if(!miembroRepository.existsById(miembro.getId())){
            throw new ManagerException("Error al modificar miembro, miembro no encontrado");
        }else{
            Miembro miembroEncontrado = miembroRepository.findByCorreo(miembro.getCorreo()).orElse(null);
            if(miembroEncontrado != null && !miembroEncontrado.getId().equals(miembro.getId()) ){
                logger.error("No se pudo guardar con exito");
                throw new ManagerException("Error al modificar miembro, correo repetido");
            }else{
                retorno =  miembroRepository.save(miembroDtoAMiembro(miembro));
                logger.debug("Miembro: " + miembro.getNombre() + " modificado con exito");
            }

        }
        return miembroAMiembroDto(retorno);
    }
    
    /**
     * Este metodo obtiene un miembro de la base de datos por su id
     * @param id
     * @return miembroDto
     * @throws ManagerException
     */
    @Override
    public MiembroDto obtenerMiembroById(Integer id)  throws ManagerException{
        Miembro miembro;
        miembro = miembroRepository.findById(id).orElse(null);
        if(miembro == null){
            throw new ManagerException("No existe el miembro");
        }
        logger.debug(miembro.getNumeroMiembro() + " encontrado con exito");
        return miembroAMiembroDto(miembro);
    }

    @Override
    public void eliminarMiembroPorId(Integer id) throws ManagerException {
        try{
            miembroRepository.deleteById(id);
            logger.debug("Miembro con id: " + id + " eliminado con exito");
        }catch(Exception e){
            throw new ManagerException("Error al eliminar miembro, miembro no encontrado");
        }
    }


    /**
     * Este metodo asigna un numero de miembro y una fecha actual al miembro.
     * La fecha se setea en la hora actual, para que por defecto el usuario no este bloqueado 
     * @param miembro
     * @return
     */
    public Miembro crearUnMiembro(Miembro miembro){
        miembro.setFechaBloqueo(LocalDateTime.now().withSecond(0).withNano(0));
        miembro.setNumeroMiembro(miembro.generarNumeroMiembro());
        logger.debug("Miembro: " + miembro.getNombre() + " creado con exito");
        return miembro;
    }
    
    @Override
    //FIXME Docuemntar
    public boolean verificarMiembroSancionado(Integer id) throws ManagerException{
    	MiembroDto miembroBuscado = obtenerMiembroById(id);
    	
    	LocalDateTime fechaSancion = dateFormatter.fechDateTime(miembroBuscado.getFechaBloqueo());
    	
    	if(LocalDateTime.now().isBefore(fechaSancion))
    		throw new ManagerException("El miembro " + miembroBuscado.getNombre() + " esta sancionado hasta la fecha " + miembroBuscado.getFechaBloqueo());
    	else
    		return false;
    }
    
    @Override
    public void sancionarMiembro(Integer id, int dias) throws ManagerException {
    	MiembroDto miembroSancionar = obtenerMiembroById(id);
    	
    	Miembro miembroGuardar = miembroDtoAMiembro(miembroSancionar);
    	miembroGuardar.setFechaBloqueo(LocalDateTime.now().withSecond(0).withNano(0).plusDays(dias));
    	
    	logger.info("Miembro" + miembroSancionar.getNombre() + "ah sido sancionado por " + dias + " dias");
    	miembroRepository.save(miembroGuardar);
    }
}
