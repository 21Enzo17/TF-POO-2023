package ar.edu.unju.fi.tp9.dto;

import ar.edu.unju.fi.tp9.util.EstadoLibro;

public class LibroEditDto {
	private Long id;
	
	private String titulo;
	
	private String autor;
	
	private EstadoLibro estado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public EstadoLibro getEstado() {
		return estado;
	}

	public void setEstado(EstadoLibro estado) {
		this.estado = estado;
	}
}
