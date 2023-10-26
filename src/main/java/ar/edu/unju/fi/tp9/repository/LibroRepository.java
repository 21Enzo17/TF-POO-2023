package ar.edu.unju.fi.tp9.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.tp9.entity.Libro;

@Repository
public interface LibroRepository extends CrudRepository<Libro, Long>{
	
	Libro findByTitulo(String titulo);
	
	Libro findByAutor(String autor);
	
	Libro findByIsbn(String isbn);
}
