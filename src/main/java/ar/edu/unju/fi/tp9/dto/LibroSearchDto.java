package ar.edu.unju.fi.tp9.dto;

import ar.edu.unju.fi.tp9.util.EstadoLibro;

public class LibroSearchDto {
	private Long id;
	
	private String titulo;
	
	private String autor;
	
	public String isbn;

	public EstadoLibro estado;
	
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

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public EstadoLibro getEstado() {
		return estado;
	}

	public void setEstado(EstadoLibro estado) {
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
