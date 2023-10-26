package ar.edu.unju.fi.tp9.entity;

import java.time.LocalDateTime;

import ar.edu.unju.fi.tp9.entity.util.EstadoPrestamo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "prestamos")
public class Prestamo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//TODO Relacion con miembro.
	
	//TODO Relacion con libro.
	
	@Column(name = "fecha_prestamo")
	private LocalDateTime fechaPrestamo;
	
	@Column(name = "fecha_devolucion")
	private LocalDateTime fechaDevolucion;

	@Enumerated(EnumType.STRING)
	private EstadoPrestamo estado;

	////////// Constructores //////////
	
	public Prestamo() {
	}

	public Prestamo(LocalDateTime fechaPrestamo, LocalDateTime fechaDevolucion, EstadoPrestamo estado) {
		this.fechaPrestamo = fechaPrestamo;
		this.fechaDevolucion = fechaDevolucion;
		this.estado = estado;
	}

	//////////Getters y Setters //////////
	
	public Long getId() {
		return id;
	}

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

	public EstadoPrestamo getEstado() {
		return estado;
	}

	public void setEstado(EstadoPrestamo estado) {
		this.estado = estado;
	}
}
