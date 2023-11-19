package ar.edu.unju.fi.tp9.dto;

import java.io.Serializable;


public class PrestamoDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long idMiembroDto;
    private Long idLibroDto;
    private String fechaPrestamo;
    private String fechaDevolucion;
    private String estado;



    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdMiembroDto() {
        return this.idMiembroDto;
    }

    public void setIdMiembroDto(Long idMiembroDto) {
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
}
