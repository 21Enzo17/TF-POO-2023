package ar.edu.unju.fi.tp9.service.imp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.dto.MiembroDto;
import ar.edu.unju.fi.tp9.dto.PrestamoDto;
import ar.edu.unju.fi.tp9.entity.Prestamo;
import ar.edu.unju.fi.tp9.enums.Estado;
import ar.edu.unju.fi.tp9.enums.EstadoLibro;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.repository.PrestamoRepository;
import ar.edu.unju.fi.tp9.service.ILibroService;
import ar.edu.unju.fi.tp9.service.IMiembroService;
import ar.edu.unju.fi.tp9.service.IPrestamoService;

@Service
public class PrestamoServiceImp implements IPrestamoService {
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    PrestamoRepository prestamoRepository;

    @Autowired
    IMiembroService miembroService;
    @Autowired
    ILibroService libroService;


    /**
     * Metodo que guarda un prestamo en la bd
     * @param prestamo
     */
    @Override
    public void guardarPrestamo(PrestamoDto prestamo)throws ManagerException {
        Prestamo prestamoGuardar;
        prestamoGuardar = prestamoDtoAPrestamo(prestamo);
        try{
            libroService.cambiarEstado(prestamo.getIdLibroDto(),EstadoLibro.PRESTADO.toString());
        }catch(ManagerException e){
            throw new ManagerException("No se pudo cambiar el estado del libro");
        }
        prestamoRepository.save(prestamoGuardar);
    }

    /**
     * Metodo que busca un prestamo por miembro
     * @param miembroDto
     */
    @Override
    public PrestamoDto buscarPrestamoPorMiembro(MiembroDto miembroDto) {
        Prestamo prestamo = prestamoRepository.findByMiembro(miembroService.miembroDtoAMiembro(miembroDto));
        return prestamoAPrestamoDto(prestamo);
    }

    /**
     * Metodo que devuelve un prestamo
     */
    @Override
    public void devolucionPrestamo(PrestamoDto prestamoDto) throws ManagerException{
        Prestamo prestamo = prestamoDtoAPrestamo(prestamoDto);
        prestamo.setEstado(Estado.DEVUELTO);
        try{
            libroService.cambiarEstado(prestamo.getLibro().getId(),EstadoLibro.DISPONIBLE.toString());
        }catch(ManagerException e){
            throw new ManagerException("No se pudo cambiar el estado del libro");
        }
        prestamoRepository.save(prestamo);
    }

    /**
     * Metodo que convierte un prestamo a prestamoDto
     * @param prestamo
     * @return
     */
    public PrestamoDto prestamoAPrestamoDto(Prestamo prestamo){
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setId(prestamo.getId());
        prestamoDto.setEstado(prestamo.getEstado().toString());
        prestamoDto.setFechaDevolucion(transformarFechaNatural(prestamo.getFechaDevolucion().toString()));
        prestamoDto.setFechaPrestamo(transformarFechaNatural(prestamo.getFechaPrestamo().toString()));
        prestamoDto.setIdMiembroDto(prestamo.getMiembro().getId());
        prestamoDto.setIdLibroDto(prestamo.getLibro().getId());
        
        
        return prestamoDto;
    }


    /**
     * Metodo que convierte un prestamoDto a prestamo
     * @param prestamoDto
     * @return
     */
    public Prestamo prestamoDtoAPrestamo(PrestamoDto prestamoDto){
        Prestamo prestamo = new Prestamo();

        if(prestamoDto.getId() == null){
            prestamo.setId(null);
        }else{
            prestamo.setId(prestamoDto.getId());
        }
        
        prestamo.setFechaDevolucion(fechDateTime(prestamoDto.getFechaDevolucion()));
        prestamo.setFechaPrestamo(fechDateTime(prestamoDto.getFechaDevolucion()));
        prestamo.setEstado(obtenerEstado(prestamoDto.getEstado()));
        prestamo.setMiembro(miembroService.miembroDtoAMiembro(miembroService.obtenerMiembroById(prestamoDto.getIdMiembroDto())));
        prestamo.setLibro(libroService.libroDtoALibro(libroService.buscarLibroPorId(prestamoDto.getIdLibroDto())));
        return prestamo;
    }


    /**
     * Metodo que obtiene el estado de un prestamo
     * @param estado
     * @return
     */
    public Estado obtenerEstado(String estado){
        Estado estadoEnum;
        switch (estado) {
            case "PRESTADO":
                estadoEnum = Estado.PRESTADO;
                break;
            case "DEVUELTO":
                estadoEnum = Estado.DEVUELTO;
                break;
            default:
                estadoEnum = null;
                break;
        }
        return estadoEnum;
    }

    public String transformarFechaNatural(String input) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(input, inputFormatter);
    
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
    
        return dateTime.format(outputFormatter);
    }

    public LocalDateTime fechDateTime(String input) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        return LocalDateTime.parse(input, inputFormatter);
    }
    
}
