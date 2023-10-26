package ar.edu.unju.fi.tp9.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class Libro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String titulo;
	
	private String autor;
	
	private String isbn;
	
	@Column(name = "num_inventario")
	private Long numeroInventario;
	
	private String estado;

	//TODO Relacion con prestamos.
	
	////////// Constructores //////////
	
	public Libro() {
	}
	
	public Libro(String titulo, String autor, String isbn, Long numeroInventario, String estado) {
		this.titulo = titulo;
		this.autor = autor;
		this.isbn = isbn;
		this.numeroInventario = numeroInventario;
		this.estado = estado;
	}
	
	////////// Getters y Setters //////////

	public Long getId() {
		return id;
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
