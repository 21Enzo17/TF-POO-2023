package ar.edu.unju.fi.tp9.service.imp;


<<<<<<< src/main/java/ar/edu/unju/fi/tp9/service/imp/PrestamoServiceImp.java
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.LocalDate;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.ResponseEntity;
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
import ar.edu.unju.fi.tp9.service.IPdfGenerator;
import ar.edu.unju.fi.tp9.service.IPrestamoService;
<<<<<<< src/main/java/ar/edu/unju/fi/tp9/service/imp/PrestamoServiceImp.java
import ar.edu.unju.fi.tp9.util.BodyGenerator;
=======
import ar.edu.unju.fi.tp9.service.IResumenService;
>>>>>>> src/main/java/ar/edu/unju/fi/tp9/service/imp/PrestamoServiceImp.java
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

    @Autowired
    IPdfGenerator pdfGenerator;
    
    @Autowired
    BodyGenerator bodyGenerator;


    @Qualifier("excelService")
    private IResumenService excelService;
    @Autowired
    @Qualifier("pdfService")
    private IResumenService pdfService;
    
    

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
     * Metodo que obtiene un prestamo y genera su correspondiente comprobante de prestamo
     * @param idPrestamo
     * @return ByteArrayOutputStream
     * @throws ManagerException
     */
    @Override
    public ByteArrayOutputStream generarComprobante(Long idPrestamo) throws ManagerException {
        PrestamoDto prestamo = obtenerPrestamoById(idPrestamo);
        if(prestamo == null){
            throw new ManagerException("No existe el prestamo con id " + idPrestamo);
        }
        return pdfGenerator.generarPdfSinGuardar(bodyGenerator.generarBodyComprobante(miembroService.obtenerMiembroById(prestamo.getIdMiembroDto()), libroService.buscarLibroPorId(prestamo.getIdLibroDto()), prestamo.getFechaPrestamo(), prestamo.getFechaDevolucion())); 
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

        String htmlBody = bodyGenerator.generarBodyCorreo(libroDto, prestamo.getFechaPrestamo(), prestamo.getFechaDevolucion());

        try{ 
            InputStreamSource inputStream = new FileSystemResource("src/main/resources/images/Bibliowlteca.png");
            emailService.send("poo2023correo@gmail.com",to, subject, htmlBody,inputStream);
            logger.info("Correo enviado correctamente");
        }catch(Exception e){
            logger.error("Error al enviar el correo: " + e.getMessage());
        }
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
        if(prestamo.getFechaDevolucion().isBefore(LocalDateTime.now())) {
        	miembroService.sancionarMiembro(prestamo.getMiembro().getId(), calcularDiasDeSancion(ChronoUnit.DAYS.between(prestamo.getFechaDevolucion(), LocalDateTime.now())));
        }
        prestamo.setFechaDevolucion(LocalDateTime.now().withSecond(0).withNano(0));
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
        prestamo.setFechaPrestamo(dateFormatter.fechDateTime(prestamoDto.getFechaPrestamo()));
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
        prestamoInfoDto.setEstado(prestamo.getEstado().toString());
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
        logger.debug("Calculando sancion para: " + dias + " dias");
        if (dias <= 2) {
            return 3;
        } else if (dias <= 5) {
            return 5;
        } else if (dias > 5) {
            return 20;
        } 
        return 0;
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


    /**
     * Metodo encargado de eliminar un prestamo por ID
     * @param id
     * @throws ManagerException
     */
    @Override
    public void eliminarPrestamoById(Long id) throws ManagerException {
        Prestamo prestamo = prestamoRepository.findById(id).orElse(null);
        if(prestamo == null){
        	logger.error("No existe el prestamo con id " + id);
            throw new ManagerException("No existe el prestamo con id " + id);
        }
        prestamoRepository.deleteById(id);
        logger.debug(prestamo.getId() + " eliminado con exito");
    }

    private List<PrestamoInfoDto> listaPrestamosEntre(LocalDateTime fechaInicio, LocalDateTime fechaFin){
    	List<Prestamo> prestamos = prestamoRepository.findByFechaPrestamoBetween(fechaInicio, fechaFin);
    	List<PrestamoInfoDto> listaDto = new ArrayList<>();
    	
    	for(Prestamo prestamo : prestamos)
    		listaDto.add(prestamoAInfoDto(prestamo));
    	return listaDto;
    }
    
	@Override
	public ResponseEntity<byte[]> realizarResumenExcel(String fechaInicio, String fechaFin) throws FileNotFoundException {	
		LocalDateTime fechaInicioFormateada = dateFormatter.fechDateTime(fechaInicio);
		LocalDateTime fechaFinalFormateada = dateFormatter.fechDateTime(fechaFin);
		
		List<PrestamoInfoDto> listaDto = listaPrestamosEntre(fechaInicioFormateada, fechaFinalFormateada);
		
		return excelService.realizarResumen(listaDto, fechaInicio, fechaFin);
	}

	@Override
	public ResponseEntity<byte[]> realizarResumenPdf(String fechaInicio, String fechaFin) throws FileNotFoundException {
		LocalDateTime fechaInicioFormateada = dateFormatter.fechDateTime(fechaInicio);
		LocalDateTime fechaFinalFormateada = dateFormatter.fechDateTime(fechaFin);
		
		List<PrestamoInfoDto> listaDto = listaPrestamosEntre(fechaInicioFormateada, fechaFinalFormateada);
		
        return pdfService.realizarResumen(listaDto, fechaInicio, fechaFin);
	}
}
