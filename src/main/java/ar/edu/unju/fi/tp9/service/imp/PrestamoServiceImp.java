package ar.edu.unju.fi.tp9.service.imp;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.tp9.dto.LibroDto;
import ar.edu.unju.fi.tp9.dto.MiembroDto;
import ar.edu.unju.fi.tp9.dto.PrestamoDto;
import ar.edu.unju.fi.tp9.dto.PrestamoInfoDto;
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
    public PrestamoInfoDto guardarPrestamo(Long idMiembro, Long idLibro)throws ManagerException {
        Prestamo prestamoGuardar;
        PrestamoDto prestamo = crearPrestamo(idMiembro, idLibro);
        prestamoGuardar = prestamoDtoAPrestamo(prestamo);
        
        validarPrestamo(prestamo);
        libroService.cambiarEstado(prestamo.getIdLibroDto(),EstadoLibro.PRESTADO.toString());
        
        prestamoGuardar = prestamoRepository.save(prestamoGuardar);
        logger.debug("Prestamo: " + prestamoGuardar.getId() +" guardado con exito");
        enviarCorreo(prestamo);
        return prestamoAInfoDto(prestamoGuardar);
    }

    /**
     * Metodo que crea un prestamo, asignando el estado inicial y las fechas de prestamo y devolucion
     * @param idMiembro
     * @param idLibro
     * @return PrestamoDto
     * @throws ManagerException
     */
    private PrestamoDto crearPrestamo(Long idMiembro, Long idLibro){
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setIdMiembroDto(idMiembro);
        prestamoDto.setIdLibroDto(idLibro); 
        prestamoDto.setEstado(Estado.PRESTADO.toString());
        prestamoDto.setFechaPrestamo(dateFormatter.transformarFechaNatural(LocalDateTime.now().withSecond(0).withNano(0).toString()));
        prestamoDto.setFechaDevolucion(dateFormatter.transformarFechaNatural(LocalDateTime.now().withSecond(0).withNano(0).plusDays(7).toString()));
        logger.info("Prestamo creado con exito con estado: " + prestamoDto.getEstado());
        return prestamoDto;
    }

    /**
     * Metodo encargado de enviar el correo al miembro
     * @param prestamo
     * @throws ManagerException
     */
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

    /**
     * Metodo que genera el body del correo
     * @param libroDto
     * @param fechaPrestamo
     * @param fechaDevolucion
     * @return
     */
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
     * Metodo que devuelve un prestamo
     * @param id
     * @throws ManagerException
     */
    @Override 
    public void devolucionPrestamo(Long id) throws ManagerException{
        Prestamo prestamo = prestamoDtoAPrestamo(obtenerPrestamoById(id));
        prestamo.setEstado(Estado.DEVUELTO);
        libroService.cambiarEstado(prestamo.getLibro().getId(),EstadoLibro.DISPONIBLE.toString());
        prestamo.setFechaDevolucion(LocalDateTime.now().withSecond(0).withNano(0));
        prestamoRepository.save(prestamo);
        logger.debug(prestamo.getId() + " devuelto con exito");
        
        if(prestamo.getFechaDevolucion().isBefore(LocalDateTime.now())) {
        	miembroService.sancionarMiembro(prestamo.getMiembro().getId(), calcularDiasDeSancion(ChronoUnit.DAYS.between(prestamo.getFechaDevolucion(), LocalDateTime.now())));
        }
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
     * Metodo que convierte un prestamo a prestamoInfoDto
     * @param prestamo
     * @return PrestamoInfoDto
     */
    private PrestamoInfoDto prestamoAInfoDto(Prestamo prestamo){
        PrestamoInfoDto prestamoInfoDto = new PrestamoInfoDto();
        prestamoInfoDto.setId(prestamo.getId());
        prestamoInfoDto.setNombreMiembro(prestamo.getMiembro().getNombre());
        prestamoInfoDto.setTituloLibro(prestamo.getLibro().getTitulo());
        prestamoInfoDto.setFechaPrestamo(dateFormatter.transformarFechaNatural(prestamo.getFechaPrestamo().toString()));
        prestamoInfoDto.setFechaDevolucion(dateFormatter.transformarFechaNatural(prestamo.getFechaDevolucion().toString()));
        logger.info("Se mapeo el prestamoInfoDto con exito");
        return prestamoInfoDto;
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
        logger.debug("Se devuelve el estado: "+ estadoEnum);
        return estadoEnum;
    }
    
    /**
     * Metodo que valida que el miembro no este sancionado y que el libro este disponible
     * @param prestamoDto
     * @throws ManagerException
     */
    private void validarPrestamo(PrestamoDto prestamoDto) throws ManagerException {
    	miembroService.verificarMiembroSancionado(prestamoDto.getIdMiembroDto());
    	libroService.verificarLibroDisponible(prestamoDto.getIdLibroDto());
    }
    
    /**
     * Metodo que calcula los dias de sancion
     * @param dias
     * @return
     */
    private int calcularDiasDeSancion(long dias) {
        if (dias > 0 && dias <= 2) {
            return 3;
        } else if (dias <= 5) {
            return 5;
        } else if (dias > 5) {
            return 20;
        } else {
            return 0;
        }
    }

    /**
     * Metodo encargado de encontrar un prestamo por ID
     * @param id
     * @return PrestamoDto
     * @throws ManagerException
     */
    @Override
    public PrestamoDto obtenerPrestamoById(Long id) throws ManagerException {
        Prestamo prestamo = prestamoRepository.findById(id).orElse(null);
        if(prestamo == null){
        	logger.error("No existe el prestamo con id " + id);
            throw new ManagerException("No existe el prestamo con id " + id);
        }
        logger.debug(prestamo.getId() + " encontrado con exito");
        return prestamoAPrestamoDto(prestamo);

    }
    
}
