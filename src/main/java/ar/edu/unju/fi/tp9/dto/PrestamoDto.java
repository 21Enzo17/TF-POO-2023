package ar.edu.unju.fi.tp9.dto;

public class PrestamoDto{
	private Long id;
	
	private LibroDto libro;
	
	private String fechaPrestamo;
	
	private String fechaDevolucion;

	private String estado;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LibroDto getLibro() {
		return libro;
	}

	public void setLibro(LibroDto libro) {
		this.libro = libro;
	}

	public String getFechaPrestamo() {
		return fechaPrestamo;
	}

	public void setFechaPrestamo(String fechaPrestamo) {
		this.fechaPrestamo = fechaPrestamo;
	}

	public String getFechaDevolucion() {
		return fechaDevolucion;
	}

	public void setFechaDevolucion(String fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
