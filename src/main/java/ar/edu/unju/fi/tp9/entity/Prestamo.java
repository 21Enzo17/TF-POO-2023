package ar.edu.unju.fi.tp9.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;

@Entity
@Table(name = "prestamos")
@Transactional
public class Prestamo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	//TODO Relacion con miembro.
	
	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_libro")
	private Libro libro  = new Libro();
	
	@Column(name = "fecha_prestamo")
	private LocalDateTime fechaPrestamo;
	
	@Column(name = "fecha_devolucion")
	private LocalDateTime fechaDevolucion;

	@Column(name = "estado")
	private String estado;

	////////// Constructores /////////////
	
	public Prestamo() {
	}
	
	public Prestamo(Long id, Libro libro, LocalDateTime fechaPrestamo, LocalDateTime fechaDevolucion, String estado) {
		this.id = id;
		this.libro = libro;
		this.fechaPrestamo = fechaPrestamo;
		this.fechaDevolucion = fechaDevolucion;
		this.estado = estado;
	}
	
	////////// Getters y Setters //////////
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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

	public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
