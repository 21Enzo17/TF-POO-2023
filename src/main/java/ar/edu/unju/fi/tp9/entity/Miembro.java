package ar.edu.unju.fi.tp9.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "miembros")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public class Miembro implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "correo")
    private String correo;

    @Column(name = "numeroTelefonico")
    private String numeroTelefonico;

    private static Integer numeroMiembroActual = 0;

    @Column(name = "numeroMiembro")
    private Integer numeroMiembro;

    @Column(name = "fechaBloqueo")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaBloqueo;




    public Miembro(String nombre, String correo, String numeroTelefonico) {
        this.nombre = nombre;
        this.correo = correo;
        this.numeroTelefonico = numeroTelefonico;
        this.numeroMiembro = numeroMiembroActual++;
        this.fechaBloqueo = LocalDateTime.now(); // Se le asigna la fecha actual, para que el bloqueo este desactivado por defecto
    }

    public Miembro() {
    }

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
        if(numeroMiembro != null){
            this.numeroMiembro = numeroMiembro;
        }
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

    public LocalDateTime getFechaBloqueo() {
        return this.fechaBloqueo;
    }

    public void setFechaBloqueo(LocalDateTime fechaBloqueo) {
        this.fechaBloqueo = fechaBloqueo;
    }

    public Miembro id(Long id) {
        setId(id);
        return this;
    }

    public Miembro nombre(String nombre) {
        setNombre(nombre);
        return this;
    }

    public Miembro correo(String correo) {
        setCorreo(correo);
        return this;
    }

    public Miembro numeroTelefonico(String numeroTelefonico) {
        setNumeroTelefonico(numeroTelefonico);
        return this;
    }


    public boolean isAlumno(){
        return false;
    }

    public boolean isDocente(){
        return false;
    }
    
    public Integer generarNumeroMiembro(){
        return numeroMiembroActual++;
    }
}
