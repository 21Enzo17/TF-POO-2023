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
import ar.edu.unju.fi.tp9.exceptions.ModelException;
import ar.edu.unju.fi.tp9.repository.MiembroRepository;
import ar.edu.unju.fi.tp9.service.IMiembroService;

@Service
public class MiembroServiceImp implements IMiembroService {
    static Logger logger = LogManager.getLogger(MiembroServiceImp.class);

    @Autowired
    MiembroRepository miembroRepository;

    /**
     * Este metodo guarda un miembro en la base de datos, comprobando antes que no se repita el correo.
     * @param miembroDto
     */
    @Override
    public void guardarMiembro(MiembroDto miembro) throws ModelException {
        if(miembroRepository.findByCorreo(miembro.getCorreo()).orElse(null) != null){
            throw new ModelException("Error al guardar alumno, correo repetido");
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
    public void eliminarMiembro(MiembroDto miembro) throws ModelException{
        logger.debug("Buscado miembro con correo: " + miembro.getCorreo());
        Miembro miembroBuscado = miembroRepository.findByCorreo(miembro.getCorreo()).orElse(null);
        if (miembroBuscado != null) {
            miembroRepository.delete(miembroBuscado);
            logger.debug(miembroBuscado.getNombre() + " eliminado con exito");
        } else {
            throw new ModelException("No se encontró ningún miembro con el correo especificado");
        }
        
    }

    /**
     * Este metodo obtiene un miembro de la base de datos por su correo
     * @param correo
     * @return miembroDto
     */
    @Override
    public MiembroDto obtenerMiembroByCorreo(String correo) throws ModelException {
        Miembro miembro = new Miembro();
        miembro = miembroRepository.findByCorreo(correo).orElse(null);
        if(miembro == null){
            throw new ModelException("No existe el miembro");
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
            miembro = new Alumno();
            miembro = modelMapper.map(miembroDto, Alumno.class);   
        }else{
            miembro = new Docente();
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
            miembroDto = new AlumnoDto();
            miembroDto = modelMapper.map(miembro, AlumnoDto.class);   
        }else{
            miembroDto = new DocenteDto();
            miembroDto = modelMapper.map(miembro, DocenteDto.class);
        }
        return miembroDto;
    }

    /**
     * En este metodo de modificacion, se presupone que el dto llega con el id del usuario a modificar, ya que se permite
     * modificar todos los atributos del miembro, ademas se checkea que el correo no este repetido
     * @param miembro
     * @return
     * @throws ModelException
     */
    @Override
    public void modificarMiembro(MiembroDto miembro) throws ModelException {
        if(!miembroRepository.existsById(miembro.getId())){
            throw new ModelException("Error al modificar miembro, miembro no encontrado");
        }else{
            Miembro miembroEncontrado = miembroRepository.findByCorreo(miembro.getCorreo()).orElse(null);
            if(miembroEncontrado != null && miembroEncontrado.getId() != miembro.getId()){
                throw new ModelException("Error al modificar miembro, correo repetido");
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
     * @throws ModelException
     */
    @Override
    public MiembroDto obtenerMiembroById(Integer id) throws ModelException{
        Miembro miembro = new Miembro();
        miembro = miembroRepository.findById(id).orElse(null);
        if(miembro == null){
            throw new ModelException("No existe el miembro");
        }
        return miembroAMiembroDto(miembro);
    }
}
