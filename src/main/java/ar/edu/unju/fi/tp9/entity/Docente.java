package ar.edu.unju.fi.tp9.entity;



import jakarta.persistence.*;

@Entity
@DiscriminatorValue(value = "docente")
public class Docente extends Miembro{
    @Column(name = "legajo")
    private String legajo;

    public Docente(String nombre, String correo, String numeroTelefonico, String legajo) {
        super(nombre, correo, numeroTelefonico);
        this.legajo = legajo;
    }

    public Docente() {
    }

    public String getLegajo() {
        return this.legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }   

    public Docente legajo(String legajo) {
        setLegajo(legajo);
        return this;
    }

    @Override
    public String toString() {
        return super.toString() + " legajo=" + legajo + "}";
    }

    @Override
    public boolean isAlumno(){
        return false;
    }

    @Override
    public boolean isDocente(){
        return true;
    }
}
