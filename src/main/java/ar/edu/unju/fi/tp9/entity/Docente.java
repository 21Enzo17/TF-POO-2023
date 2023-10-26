package ar.edu.unju.fi.tp9.entity;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue(value = "docente")
public class Docente extends Miembro{
    private static Logger logger = LogManager.getLogger(Miembro.class);
    @Column(name = "legajo")
    private String legajo;

    public Docente(String nombre, String correo, String numeroTelefonico, String legajo) {
        super(nombre, correo, numeroTelefonico);
        this.legajo = legajo;
        logger.info("Docente: "+nombre+" creado");
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


}
