package ar.edu.unju.fi.tp9.dto;

public class DocenteDto extends MiembroDto {
    private String legajo;

    public String getLegajo() {
        return this.legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
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
