package ar.edu.unju.fi.tp9.entity;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name = "miembros")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public class Miembro implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "correo", unique = true)
    private String correo;

    @Column(name = "numeroTelefonico")
    private String numeroTelefonico;

    public Miembro(String nombre, String correo, String numeroTelefonico) {
        this.nombre = nombre;
        this.correo = correo;
        this.numeroTelefonico = numeroTelefonico;
    }

    public Miembro() {
    }

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

    public Miembro id(Integer id) {
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

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", correo='" + getCorreo() + "'" +
            ", numeroTelefonico='" + getNumeroTelefonico() + "'" 
            ;
    }
    
}
