package ar.edu.unju.fi.tp9.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.tp9.entity.Libro;

@Repository
public interface LibroRepository extends CrudRepository<Libro, Long>{
	
	boolean existsByIsbn(String isbn);
	
	Libro findByTitulo(String titulo);
	
	List<Libro> findAllByAutor(String autor);
	
	Libro findByIsbn(String isbn);
}
