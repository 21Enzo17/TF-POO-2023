package ar.edu.unju.fi.tp9.dto;

import ar.edu.unju.fi.tp9.util.EstadoLibro;

public class LibroEditDto extends LibroBaseDto{
	
	private String autor;
	
	private EstadoLibro estado;

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
