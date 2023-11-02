package ar.edu.unju.fi.tp9.dto;

import java.io.Serializable;

public class MiembroDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String nombre;
    private String correo;
    private String numeroTelefonico;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return this.correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumeroTelefonico() {
        return this.numeroTelefonico;
    }

    public void setNumeroTelefonico(String numeroTelefonico) {
        this.numeroTelefonico = numeroTelefonico;
    }

    public MiembroDto id(Integer id) {
        setId(id);
        return this;
    }

    public MiembroDto nombre(String nombre) {
        setNombre(nombre);
        return this;
    }

    public MiembroDto correo(String correo) {
        setCorreo(correo);
        return this;
    }

    public MiembroDto numeroTelefonico(String numeroTelefonico) {
        setNumeroTelefonico(numeroTelefonico);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", correo='" + getCorreo() + "'" +
            ", numeroTelefonico='" + getNumeroTelefonico() + "'" +
            "}";
    }

    public boolean isAlumno(){
        return false;
    }

    public boolean isDocente(){
        return false;
    }
    
    
}
