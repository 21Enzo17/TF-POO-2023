package ar.edu.unju.fi.tp9.dto;

import java.io.Serializable;


public class PrestamoInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nombreMiembro;
    private String tituloLibro;
    private String fechaPrestamo;
    private String fechaDevolucion;

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getNombreMiembro() {
        return this.nombreMiembro;
    }

    public void setNombreMiembro(String nombreMiembro) {
        this.nombreMiembro = nombreMiembro;
    }

    public String getTituloLibro() {
        return this.tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public String getFechaPrestamo() {
        return this.fechaPrestamo;
    }

    public void setFechaPrestamo(String fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public String getFechaDevolucion(){
        return this.fechaDevolucion;
    }

    public void setFechaDevolucion(String fechaDevolucion){
        this.fechaDevolucion = fechaDevolucion;
    }


   
}
