package ar.edu.unju.fi.tp9.dto;

import ar.edu.unju.fi.tp9.util.EstadoLibro;

public class LibroBuscarDto extends LibroBaseDto{
	private String autor;
	
	public String isbn;

	public String estado;

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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
