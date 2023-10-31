package ar.edu.unju.fi.tp9.dto;

public class LibroSaveDto extends LibroBaseDto{
	
	private String autor;
	
	private String isbn;
	
	private Long numeroInventario;

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
