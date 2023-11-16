package ar.edu.unju.fi.tp9.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LibroDto implements Serializable{
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "id")
	private Long id;

	@JsonProperty(value = "titulo")
	private String titulo;

	@JsonProperty(value = "autor")
	private String autor;
	
	@JsonProperty(value = "isbn")
	private String isbn;
	
	@JsonProperty(value = "numeroInventario")
	private Long numeroInventario;
	
	@JsonProperty(value = "estado")
	private String estado;
	
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

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Long getNumeroInventario() {
		return numeroInventario;
	}

	public void setNumeroInventario(Long numeroInventario) {
		this.numeroInventario = numeroInventario;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
