package ar.edu.unju.fi.tp9.service.imp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.dto.MiembroDto;
import ar.edu.unju.fi.tp9.dto.PrestamoDto;
import ar.edu.unju.fi.tp9.entity.Prestamo;
import ar.edu.unju.fi.tp9.enums.Estado;
import ar.edu.unju.fi.tp9.repository.PrestamoRepository;
import ar.edu.unju.fi.tp9.service.IMiembroService;
import ar.edu.unju.fi.tp9.service.IPrestamoService;

@Service
public class PrestamoServiceImp implements IPrestamoService {
    static Logger logger = LogManager.getLogger(MiembroServiceImp.class);
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    PrestamoRepository prestamoRepository;

    @Autowired
    IMiembroService miembroService;

    @Override
    public void guardarPrestamo(PrestamoDto prestamo) {
        Prestamo prestamoGuardar;
        prestamoGuardar = prestamoDtoAPrestamo(prestamo);
        prestamoRepository.save(prestamoGuardar);
    }

    @Override
    public PrestamoDto buscarPrestamoPorMiembro(MiembroDto miembroDto) {
        Prestamo prestamo = prestamoRepository.findByMiembro(miembroService.miembroDtoAMiembro(miembroDto));
        return prestamoAPrestamoDto(prestamo);
    }

    @Override
    public void devolucionPrestamo(PrestamoDto prestamoDto){
        Prestamo prestamo = prestamoDtoAPrestamo(prestamoDto);
        prestamo.setEstado(Estado.DEVUELTO);
        prestamoRepository.save(prestamo);
    }

    public PrestamoDto prestamoAPrestamoDto(Prestamo prestamo){
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setId(prestamo.getId());
        prestamoDto.setEstado(prestamo.getEstado().toString());
        prestamoDto.setFechaDevolucion(prestamo.getFechaDevolucion().toString());
        prestamoDto.setFechaPrestamo(prestamo.getFechaPrestamo().toString());
        prestamoDto.setMiembroDto(miembroService.miembroAMiembroDto(prestamo.getMiembro()));
        return prestamoDto;
    }

    public Prestamo prestamoDtoAPrestamo(PrestamoDto prestamoDto){
        Prestamo prestamo = new Prestamo();
        if(prestamoDto.getId() == null){
            prestamo.setId(null);
        }else{
            prestamo.setId(prestamoDto.getId());
        }
        
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        prestamo.setFechaDevolucion(LocalDateTime.parse(prestamoDto.getFechaDevolucion(), formatter));
        prestamo.setFechaPrestamo(LocalDateTime.parse(prestamoDto.getFechaPrestamo(), formatter));
        prestamo.setEstado(obtenerEstado(prestamoDto.getEstado()));
        prestamo.setMiembro(miembroService.miembroDtoAMiembro(prestamoDto.getMiembroDto()));
        return prestamo;
    }

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

    
    
}