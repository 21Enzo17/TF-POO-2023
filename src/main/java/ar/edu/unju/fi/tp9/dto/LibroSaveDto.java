package ar.edu.unju.fi.tp9.dto;

public class LibroSaveDto {
	
	private String titulo;
	
	private String autor;
	
	private String isbn;
	
	private Long numeroInventario;

	public LibroSaveDto(String titulo, String autor, String isbn, Long numeroInventario) {
		this.titulo = titulo;
		this.autor = autor;
		this.isbn = isbn;
		this.numeroInventario = numeroInventario;
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
}
