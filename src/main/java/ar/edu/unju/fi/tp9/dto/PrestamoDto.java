package ar.edu.unju.fi.tp9.dto;

import java.io.Serializable;
import java.util.Objects;


public class PrestamoDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer idMiembroDto;
    private Long idLibroDto;
    private String fechaPrestamo;
    private String fechaDevolucion;
    private String estado;



    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdMiembroDto() {
        return this.idMiembroDto;
    }

    public void setIdMiembroDto(Integer idMiembroDto) {
        this.idMiembroDto = idMiembroDto;
    }

    public Long getIdLibroDto() {
        return this.idLibroDto;
    }

    public void setIdLibroDto(Long idLibroDto) {
        this.idLibroDto = idLibroDto;
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

    public PrestamoDto id(Integer id) {
        setId(id);
        return this;
    }

    public PrestamoDto idMiembroDto(Integer idMiembroDto) {
        setIdMiembroDto(idMiembroDto);
        return this;
    }

    public PrestamoDto idLibroDto(Long idLibroDto) {
        setIdLibroDto(idLibroDto);
        return this;
    }

    public PrestamoDto fechaPrestamo(String fechaPrestamo) {
        setFechaPrestamo(fechaPrestamo);
        return this;
    }

    public PrestamoDto fechaDevolucion(String fechaDevolucion) {
        setFechaDevolucion(fechaDevolucion);
        return this;
    }

    public PrestamoDto estado(String estado) {
        setEstado(estado);
        return this;
    }

   
    


}
