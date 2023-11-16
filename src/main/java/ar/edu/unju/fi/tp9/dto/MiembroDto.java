package ar.edu.unju.fi.tp9.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME, 
  include = JsonTypeInfo.As.PROPERTY, 
  property = "tipo")
@JsonSubTypes({
  @JsonSubTypes.Type(value = AlumnoDto.class, name = "alumno"),
  @JsonSubTypes.Type(value = DocenteDto.class, name = "docente")
})
public class MiembroDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Integer numeroMiembro;
    private String nombre;
    private String correo;
    private String numeroTelefonico;
    private String fechaBloqueo;



    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroMiembro(){
        return this.numeroMiembro;
    }

    public void setNumeroMiembro(Integer numeroMiembro){
        this.numeroMiembro = numeroMiembro;
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

    public String getFechaBloqueo() {
        return this.fechaBloqueo;
    }

    public void setFechaBloqueo(String fechaBloqueo) {
        this.fechaBloqueo = fechaBloqueo;
    }

    public boolean isAlumno(){
        return false;
    }

    public boolean isDocente(){
        return false;
    }
    
    
}
