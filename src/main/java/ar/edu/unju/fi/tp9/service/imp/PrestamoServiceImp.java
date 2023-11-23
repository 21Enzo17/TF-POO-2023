package ar.edu.unju.fi.tp9.service.imp;



import java.io.ByteArrayOutputStream;

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
import ar.edu.unju.fi.tp9.service.IComprobanteGenerator;
import ar.edu.unju.fi.tp9.service.IPrestamoService;

import ar.edu.unju.fi.tp9.util.BodyGenerator;

import ar.edu.unju.fi.tp9.service.IResumenService;

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
    IComprobanteGenerator comprobanteGenerator;
    
    @Autowired
    BodyGenerator bodyGenerator;

    @Autowired
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
        Prestamo prestamoGuardar = crearPrestamo(idMiembro, idLibro);
        
        validarPrestamo(prestamoGuardar);
        libroService.cambiarEstado(prestamoGuardar.getLibro(),EstadoLibro.PRESTADO.toString());
        
        prestamoGuardar = prestamoRepository.save(prestamoGuardar);
        logger.info("Prestamo con id: " + prestamoGuardar.getId() +" guardado con exito para el miembro con id: " + idMiembro + " y el libro con id: " + idLibro);
        
        enviarCorreo(prestamoGuardar, null,false);
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
        Prestamo prestamo = prestamoRepository.findById(idPrestamo).orElse(null);
        if(prestamo == null){
            logger.error("No se encontro el prestamo con id " + idPrestamo + " para generar el comprobante");
            throw new ManagerException("No existe el prestamo con id " + idPrestamo);
        }
        logger.info("Generando comprobante de prestamo para el prestamo con id: " + idPrestamo + "...");
        return comprobanteGenerator.generarComprobante(bodyGenerator.generarBodyComprobante(prestamo.getMiembro(), 
        prestamo.getLibro(), prestamo.getFechaPrestamo(), prestamo.getFechaDevolucion(), prestamo.getEstado().toString())); 
    }

    /**
     * Metodo que devuelve un prestamo
     * @param id
     * @throws ManagerException
     */
    @Override 
    public void devolucionPrestamo(Long id) throws ManagerException{
        String fechaSancion = null;
       
        Prestamo prestamo = prestamoRepository.findById(id).orElse(null);
        if(prestamo == null){
            logger.error("No existe el prestamo con id " + id);
            throw new ManagerException("Prestamo con id: " + id + " no ha sido registrado");
        }
        	
        
        prestamo.setEstado(Estado.DEVUELTO);
        libroService.cambiarEstado(prestamo.getLibro(),EstadoLibro.DISPONIBLE.toString());
        
        if(prestamo.getFechaDevolucion().isBefore(LocalDateTime.now())) {
        	miembroService.sancionarMiembro(prestamo.getMiembro(), calcularDiasDeSancion(ChronoUnit.DAYS.between(prestamo.getFechaDevolucion(), LocalDateTime.now())));
            fechaSancion = miembroService.obtenerMiembroById(prestamo.getMiembro().getId()).getFechaBloqueo();
            logger.info("Miembro con id " + prestamo.getMiembro().getId()  + " sancionado hasta: " + fechaSancion);
        }
        
        prestamo.setFechaDevolucion(LocalDateTime.now().withSecond(0).withNano(0));
        
        prestamoRepository.save(prestamo);
        
        enviarCorreo(prestamo, fechaSancion, true);
        logger.info("Prestamo con id: " + prestamo.getId() + " devuelto con exito para el miembro con id: " + prestamo.getMiembro().getId() + " y el libro con id: " + prestamo.getLibro().getId() + ".");
        
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
        logger.info("Prestamo con id: " + prestamo.getId() + " encontrado con exito");
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
        logger.info("Prestamo con id: " + prestamo.getId() + " eliminado con exito");
    }

    /**
     * Metodo encargado de generar un resumen de prestamos en formato excel
     * @param fechaInicio
     * @param fechaFin
     * @return ResponseEntity<byte[]>
     */
	@Override
	public ResponseEntity<byte[]> realizarResumenExcel(String fechaInicio, String fechaFin) throws ManagerException{	
		LocalDateTime fechaInicioFormateada = dateFormatter.fechDateTime(fechaInicio);
		LocalDateTime fechaFinalFormateada = dateFormatter.fechDateTime(fechaFin);
		
		List<PrestamoInfoDto> listaDto = listaPrestamosEntre(fechaInicioFormateada, fechaFinalFormateada);
		logger.info("Generando resumen de prestamos con excel");
		return excelService.realizarResumen(listaDto, fechaInicio, fechaFin);
	}


    /**
     * Metodo encargado de generar un resumen de prestamos en formato pdf
     * @param fechaInicio
     * @param fechaFin
     * @return ResponseEntity<byte[]>
     */
	@Override
	public ResponseEntity<byte[]> realizarResumenPdf(String fechaInicio, String fechaFin) throws ManagerException {
		LocalDateTime fechaInicioFormateada = dateFormatter.fechDateTime(fechaInicio);
		LocalDateTime fechaFinalFormateada = dateFormatter.fechDateTime(fechaFin);
		
		List<PrestamoInfoDto> listaDto = listaPrestamosEntre(fechaInicioFormateada, fechaFinalFormateada);
		
        logger.info("Generando resumen de prestamos con pdf");
        return pdfService.realizarResumen(listaDto, fechaInicio, fechaFin);
	}

    /**
     * Metodo que crea un prestamo, asignando el estado inicial y las fechas de prestamo y devolucion
     * @param idMiembro
     * @param idLibro
     * @return PrestamoDto
     * @throws ManagerException
     */
    private Prestamo crearPrestamo(Long idMiembro, Long idLibro) throws ManagerException{
        Prestamo prestamo = new Prestamo();
        prestamo.setMiembro(miembroService.miembroDtoAMiembro(miembroService.obtenerMiembroById(idMiembro)));
        prestamo.setLibro(libroService.libroDtoALibro(libroService.buscarLibroPorId(idLibro))); 
        prestamo.setEstado(Estado.PRESTADO);
        prestamo.setFechaPrestamo(LocalDateTime.now().withSecond(0).withNano(0));
        prestamo.setFechaDevolucion(LocalDateTime.now().withSecond(0).withNano(0).plusDays(7));
        return prestamo;
    }

    /**
     * Metodo encargado de enviar el correo al miembro
     * @param prestamo
     * @throws ManagerException
     */
    private void enviarCorreo(Prestamo prestamo,String fechaSancion, Boolean devolucion) throws ManagerException{
        String to = prestamo.getMiembro().getCorreo();
        String subject = "Prestamo: " + prestamo.getLibro().getTitulo();
        String htmlBody;
        if(!devolucion){
            htmlBody = bodyGenerator.generarBodyCorreo(prestamo.getLibro(),prestamo.getFechaPrestamo(), prestamo.getFechaDevolucion());
        }else{
            htmlBody = bodyGenerator.generarBodyCorreoDevolucion(prestamo.getLibro(),prestamo.getFechaPrestamo(), prestamo.getFechaDevolucion(), fechaSancion);
        }

        try{ 
            InputStreamSource inputStream = new FileSystemResource("src/main/resources/images/Bibliowlteca.png");
            emailService.send("poo2023correo@gmail.com",to, subject, htmlBody,inputStream, generarComprobante(prestamo.getId()));
        }catch(Exception e){
            logger.error("Error al enviar el correo: " + e.getMessage());
        }
    }



    /**
     * Metodo que convierte un prestamo a prestamoDto
     * @param prestamo
     * @return
     */
    private PrestamoDto prestamoAPrestamoDto(Prestamo prestamo){
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setId(prestamo.getId());
        prestamoDto.setEstado(prestamo.getEstado().toString());
        prestamoDto.setFechaDevolucion(dateFormatter.transformarFechaNatural(prestamo.getFechaDevolucion().toString()));
        prestamoDto.setFechaPrestamo(dateFormatter.transformarFechaNatural(prestamo.getFechaPrestamo().toString()));
        prestamoDto.setIdMiembroDto(prestamo.getMiembro().getId());
        prestamoDto.setIdLibroDto(prestamo.getLibro().getId());
        return prestamoDto;
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
        return prestamoInfoDto;
    }
    
    /**
     * Metodo que valida que el miembro no este sancionado y que el libro este disponible
     * @param prestamoDto
     * @throws ManagerException
     */
    private void validarPrestamo(Prestamo prestamo) throws ManagerException {
    	miembroService.verificarMiembroSancionado(prestamo.getMiembro());
    	libroService.verificarLibroDisponible(prestamo.getLibro());
    }
    
    /**
     * Metodo que calcula los dias de sancion
     * @param dias
     * @return
     */
    private int calcularDiasDeSancion(long dias) {
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
     * Metodo encargado de buscar todos los prestamos entre dos fechas y retornarlo como una lista de PrestamoInfoDto
     * @param fechaInicio
     * @param fechaFin
     * @return List<PrestamoInfoDto>
     */
    private List<PrestamoInfoDto> listaPrestamosEntre(LocalDateTime fechaInicio, LocalDateTime fechaFin){
    	List<Prestamo> prestamos = prestamoRepository.findByFechaPrestamoBetween(fechaInicio, fechaFin);
    	List<PrestamoInfoDto> listaDto = new ArrayList<>();
    	logger.info("Se encontraron " + prestamos.size() + " prestamos");
    	for(Prestamo prestamo : prestamos)
    		listaDto.add(prestamoAInfoDto(prestamo));
    	return listaDto;
    }  
}
