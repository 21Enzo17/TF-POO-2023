package ar.edu.unju.fi.tp9.dto;

import java.io.Serializable;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class PrestamoDto implements Serializable {
    static Logger logger = LogManager.getLogger(PrestamoDto.class);
    private static final long serialVersionUID = 1L;
    private Integer id;
    private MiembroDto miembroDto;
    private LibroDto libroDto;
    private String fechaPrestamo;
    private String fechaDevolucion;
    private String estado;


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LibroDto getLibroDto() {
        return this.libroDto;
    }

    public void setLibroDto(LibroDto libroDto){
        this.libroDto = libroDto;
    }

    public MiembroDto getMiembroDto() {
        return this.miembroDto;
    }

    public void setMiembroDto(MiembroDto miembroDto) {
        this.miembroDto = miembroDto;
    }

    public String getFechaPrestamo() {
        return this.fechaPrestamo;
    }

    public void setFechaPrestamo(String fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public String getFechaDevolucion() {
        return this.fechaDevolucion;
    }

    public void setFechaDevolucion(String fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


}
