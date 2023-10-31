package ar.edu.unju.fi.tp9.dto;

import ar.edu.unju.fi.tp9.util.EstadoLibro;

public class LibroSearchDto extends LibroBaseDto{
	private String autor;
	
	public String isbn;

	public EstadoLibro estado;

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
}
