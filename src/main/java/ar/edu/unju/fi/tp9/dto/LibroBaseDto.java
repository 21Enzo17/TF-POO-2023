package ar.edu.unju.fi.tp9.dto;

public abstract class LibroBaseDto {
	private Long id;

	private String titulo;
	
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
}
