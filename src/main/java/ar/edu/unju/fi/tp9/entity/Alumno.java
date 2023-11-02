package ar.edu.unju.fi.tp9.entity;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue(value = "alumno")
public class Alumno extends Miembro{
    private static Logger logger = LogManager.getLogger(Miembro.class);

    @Column(name = "libretaUniversitaria")
    private String libretaUniversitaria;

    

    public Alumno(String nombre, String correo, String numeroTelefonico, String libretaUniversitaria) {
        super(nombre, correo, numeroTelefonico);
        this.libretaUniversitaria = libretaUniversitaria;
        logger.info("Alumno: "+nombre+" creado");
    }


    public Alumno() {
    }

    public Alumno(String libretaUniversitaria) {
        this.libretaUniversitaria = libretaUniversitaria;
    }

    public String getLibretaUniversitaria() {
        return this.libretaUniversitaria;
    }

    public void setLibretaUniversitaria(String libretaUniversitaria) {
        this.libretaUniversitaria = libretaUniversitaria;
    }

    public Alumno libretaUniversitaria(String libretaUniversitaria) {
        setLibretaUniversitaria(libretaUniversitaria);
        return this;
    }

    @Override
    public String toString() {
        return super.toString() + " libretaUniversitaria=" + libretaUniversitaria + "}";
    }
    
    
    @Override
    public boolean isAlumno(){
        return true;
    }

    @Override
    public boolean isDocente(){
        return false;
    }
}
