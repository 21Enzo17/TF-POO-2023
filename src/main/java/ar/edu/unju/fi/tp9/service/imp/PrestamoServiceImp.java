package ar.edu.unju.fi.tp9.service.imp;


import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.dto.LibroDto;
import ar.edu.unju.fi.tp9.dto.MiembroDto;
import ar.edu.unju.fi.tp9.dto.PrestamoDto;
import ar.edu.unju.fi.tp9.entity.Prestamo;
import ar.edu.unju.fi.tp9.enums.Estado;
import ar.edu.unju.fi.tp9.enums.EstadoLibro;
import ar.edu.unju.fi.tp9.exception.ManagerException;
import ar.edu.unju.fi.tp9.repository.PrestamoRepository;
import ar.edu.unju.fi.tp9.service.IEmailService;
import ar.edu.unju.fi.tp9.service.ILibroService;
import ar.edu.unju.fi.tp9.service.IMiembroService;
import ar.edu.unju.fi.tp9.service.IPrestamoService;
import ar.edu.unju.fi.tp9.util.DateFormatter;

@Service
public class PrestamoServiceImp implements IPrestamoService {
    static Logger logger = Logger.getLogger(PrestamoServiceImp.class);

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    PrestamoRepository prestamoRepository;

    @Autowired
    IMiembroService miembroService;
    @Autowired
    ILibroService libroService;
    @Autowired
    IEmailService emailService;
    @Autowired
    DateFormatter dateFormatter;

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
        
        prestamoGuardar = prestamoRepository.save(prestamoGuardar);
        logger.debug("Prestamo: " + prestamoGuardar.getId() +"guardado con exito");
        enviarCorreo(prestamo);
    }


    public void enviarCorreo(PrestamoDto prestamo) throws ManagerException{
        MiembroDto miembroDto = miembroService.obtenerMiembroById(prestamo.getIdMiembroDto());
        LibroDto libroDto = libroService.buscarLibroPorId(prestamo.getIdLibroDto());
        String to = miembroDto.getCorreo();
        String subject = "Prestamo: " + libroDto.getTitulo();

        String htmlBody = generarBody(libroDto, prestamo.getFechaPrestamo(), prestamo.getFechaDevolucion());

        try{ 
            InputStreamSource inputStream = new FileSystemResource("src/main/resources/images/Bibliowlteca.png");
            emailService.send("poo2023correo@gmail.com",to, subject, htmlBody,inputStream);
            logger.info("Correo enviado correctamente");
        }catch(Exception e){
            logger.error("Error al enviar el correo: " + e.getMessage());
        }
    }

    public String generarBody(LibroDto libroDto, String fechaPrestamo, String fechaDevolucion){
        String htmlBody = "<html><body><div style='text-align: center;'>" +
            "<h1>Bibliowlteca</h1>" +
            "<img src=\"cid:logo\" style=\"width: 200px; display: block; margin: 0 auto; border-radius: 20px;\" />"  +
            "<h2>Informacion del prestamo</h2>"  +
            "<p><b>Titulo:</b> "+ libroDto.getTitulo() + "</p>"+ 
            "<p><b>ISBN:</b> " + libroDto.getIsbn() + "</p>" +
            "<p><b>Fecha de prestamo:</b> "+ fechaPrestamo  + "</p>" +
            "<p><b>Fecha de devolucion:</b> "+ fechaDevolucion + "</p>" +
            "<h6>Correo generado automaticamente</h6>" + 
            "</div></body></html>";
            logger.info("Body generado correctamente");
        return htmlBody;
    }

    /**
     * Metodo que busca un prestamo por miembro
     * @param miembroDto
     */
    @Override
    public PrestamoDto buscarPrestamoPorMiembro(MiembroDto miembroDto) {
        // FIXME: Deberia retornar una lista de miembros, ademas solo es necesario el id del miembro
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
        logger.debug(prestamo.getId() + " devuelto con exito");
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
        prestamoDto.setFechaDevolucion(dateFormatter.transformarFechaNatural(prestamo.getFechaDevolucion().toString()));
        prestamoDto.setFechaPrestamo(dateFormatter.transformarFechaNatural(prestamo.getFechaPrestamo().toString()));
        prestamoDto.setIdMiembroDto(prestamo.getMiembro().getId());
        prestamoDto.setIdLibroDto(prestamo.getLibro().getId());
        
        logger.info("Prestamo mapeado con exito");
        return prestamoDto;
    }


    /**
     * Metodo que convierte un prestamoDto a prestamo
     * @param prestamoDto
     * @return
     */
    public Prestamo prestamoDtoAPrestamo(PrestamoDto prestamoDto) throws ManagerException{
        Prestamo prestamo = new Prestamo();

        if(prestamoDto.getId() == null){
            prestamo.setId(null);
        }else{
            prestamo.setId(prestamoDto.getId());
        }
        
        prestamo.setFechaDevolucion(dateFormatter.fechDateTime(prestamoDto.getFechaDevolucion()));
        prestamo.setFechaPrestamo(dateFormatter.fechDateTime(prestamoDto.getFechaDevolucion()));
        prestamo.setEstado(obtenerEstado(prestamoDto.getEstado()));
        prestamo.setMiembro(miembroService.miembroDtoAMiembro(miembroService.obtenerMiembroById(prestamoDto.getIdMiembroDto())));
        prestamo.setLibro(libroService.libroDtoALibro(libroService.buscarLibroPorId(prestamoDto.getIdLibroDto())));
        logger.info("Prestamo mapeado con exito");
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


    
}
