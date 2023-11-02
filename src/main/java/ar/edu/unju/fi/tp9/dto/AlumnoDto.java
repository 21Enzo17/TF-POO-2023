package ar.edu.unju.fi.tp9.dto;

public class AlumnoDto extends MiembroDto {
    private String libretaUniversitaria;

    public String getLibretaUniversitaria() {
        return this.libretaUniversitaria;
    }

    public void setLibretaUniversitaria(String libretaUniversitaria) {
        this.libretaUniversitaria = libretaUniversitaria;
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
