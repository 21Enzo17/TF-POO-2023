package ar.edu.unju.fi.tp9.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.tp9.entity.Libro;

@Repository
public interface LibroRepository extends CrudRepository<Libro, Long>{
	Libro findByTitulo(String titulo);
	
	List<Libro> findAllByIsbn(String isbn);
	
	List<Libro> findAllByAutor(String autor);
	
	Libro findByIsbnAndNumeroInventario(String isbn, Long numeroInventario);
}
