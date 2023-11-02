package ar.edu.unju.fi.tp9.entity;

import ar.edu.unju.fi.tp9.enums.Estado;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "prestamos")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @OneToOne
    @JoinColumn(name="id_miembro")
    private Miembro miembro;
    /*@OneToOne
    @JoinColumn(name="id_libro")
    private Libro libro;*/
    @Column(name = "fecha_prestamo")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaPrestamo;
    @Column(name = "fecha_devolucion")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaDevolucion;
    @Column(name = "estado")
    private Estado estado;

    public Prestamo() {
    }

    public Prestamo(Miembro miembro, LocalDateTime fechaPrestamo, LocalDateTime fechaDevolucion, Estado estado) {
        this.miembro = miembro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }

    public String formatearFecha(LocalDateTime fecha) {
        return fecha.getDayOfMonth() + "/" + fecha.getMonthValue() + "/" + fecha.getYear() + " " + fecha.getHour() + ":" + fecha.getMinute() + ":" + fecha.getSecond();

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Miembro getMiembro() {
        return miembro;
    }

    public void setMiembro(Miembro miembro) {
        this.miembro = miembro;
    }

   /* public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }*/

    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "id=" + id +
                ", miembro=" + miembro +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaDevolucion=" + fechaDevolucion +
                ", estado=" + estado +
                '}';
    }
}
