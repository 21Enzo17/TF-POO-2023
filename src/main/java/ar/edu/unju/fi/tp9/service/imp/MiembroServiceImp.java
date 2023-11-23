package ar.edu.unju.fi.tp9.service.imp;


import java.time.LocalDateTime;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
            logger.error("Error al guardar alumno, correo repetido");
            throw new ManagerException("Error al guardar alumno, correo repetido");
        }else{
            miembroReturn = miembroRepository.save(crearUnMiembro(miembroDtoAMiembro(miembro)));
            logger.info("Miembro: " + miembro.getNombre() + " guardado con exito");
        }
        return miembroAMiembroDto(miembroReturn);
    }

    /**
     * Este metodo elimina un miembro de la base de datos
     * @param miembroDto
     */
    @Override
    public void eliminarMiembroPorCorreo(String correo) throws ManagerException{
        logger.info("Buscado miembro con correo: " + correo);
        Miembro miembroBuscado = miembroRepository.findByCorreo(correo).orElse(null);
        if (miembroBuscado != null) {
            miembroRepository.delete(miembroBuscado);
            logger.info("Miembro con correo: " + correo + " eliminado con exito");
        } else {
        	logger.error("No se encontró ningún miembro con el correo: " + correo);
            throw new ManagerException("No se encontró ningún miembro con el correo: " + correo);
        }
        
    }
    /**
     * Este metodo elimina un miembro de la base de datos por su id
     * @param miembroDto
     * @throws ManagerException
     */
    @Override
    public void eliminarMiembroPorId(Long id) throws ManagerException {
        try{
            miembroRepository.deleteById(id);
            logger.info("Miembro con id: " + id + " eliminado con exito");
        } catch(DataIntegrityViolationException e){
            logger.error("Error al eliminar miembro, miembro tiene préstamos asociados");
            throw new ManagerException("Miembro con id: " + id  + " tiene préstamos asociados");
        } catch(Exception e){
            logger.error("Error al eliminar miembro, miembro no encontrado");
            throw new ManagerException("Miembro con id: " + id  + " no encontrado");
        }
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
                logger.error("Error al modificar miembro, correo repetido");
                throw new ManagerException("Error al modificar miembro, correo repetido");
            }else{
                retorno =  miembroRepository.save(miembroDtoAMiembro(miembro));
                logger.info("Miembro: " + miembro.getNombre() + " modificado con exito");
            }

        }
        return miembroAMiembroDto(retorno);
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
        	logger.error("No existe el miembro registrado con el correo " + correo);
            throw new ManagerException("No existe el miembro registrado con el correo " + correo);
        }
        logger.info("Miembro con id: " + miembro.getId() + " encontrado con exito");
        return miembroAMiembroDto(miembro);
    }

    /**
     * Este metodo obtiene un miembro de la base de datos por su id
     * @param id
     * @return miembroDto
     * @throws ManagerException
     */
    @Override
    public MiembroDto obtenerMiembroById(Long id)  throws ManagerException{
        Miembro miembro;
        miembro = miembroRepository.findById(id).orElse(null);
        if(miembro == null){
        	logger.error("No existe el miembro con id: " + id);
            throw new ManagerException("No existe el miembro con id: " + id);
        }
        logger.info("Miembro con id: " + miembro.getId() + " encontrado con exito");
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
        return miembroDto;
    }
       
    @Override
    /**
     * Metodo que veridica si un miembro existe y si esta sancionado, en caso de estar sancionado
     * lanza una excepcion personalizada
     * @param id
     * @throws ManagerException
     */
    public void verificarMiembroSancionado(Miembro miembro) throws ManagerException{
    	
    	LocalDateTime fechaSancion = miembro.getFechaBloqueo();
    	
    	if(LocalDateTime.now().isBefore(fechaSancion)){
            logger.error("El miembro esta sancionado");
    		throw new ManagerException("El miembro " + miembro.getNombre() + " esta sancionado hasta la fecha " + dateFormatter.transformarFechaNatural(miembro.getFechaBloqueo().toString()));
        }
    }

    /**
     * Metodo que sanciona a un miembro por una cantidad de dias
     * @param id
     * @param dias
     * @throws ManagerException
     */
    @Override
    public void sancionarMiembro(Miembro miembro, int dias) throws ManagerException {    	
    	miembro.setFechaBloqueo(LocalDateTime.now().withSecond(0).withNano(0).plusDays(dias));
    	
    	logger.info("Miembro " + miembro.getNombre() + " ha sido sancionado por " + dias + " dias");
    	miembroRepository.save(miembro);
    }

    /**
     * Este metodo asigna un numero de miembro y una fecha actual al miembro.
     * La fecha se setea en la hora actual, para que por defecto el usuario no este bloqueado 
     * @param miembro
     * @return
     */
    private Miembro crearUnMiembro(Miembro miembro){
        miembro.setFechaBloqueo(LocalDateTime.now().withSecond(0).withNano(0));
        miembro.setNumeroMiembro(miembro.generarNumeroMiembro());
        logger.info("Miembro: " + miembro.getNombre() + " creado con exito");
        return miembro;
    }
    
}
