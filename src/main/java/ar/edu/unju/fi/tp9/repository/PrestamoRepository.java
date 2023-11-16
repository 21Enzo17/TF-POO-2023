package ar.edu.unju.fi.tp9.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.tp9.entity.Miembro;
import ar.edu.unju.fi.tp9.entity.Prestamo;

@Repository
public interface PrestamoRepository extends CrudRepository<Prestamo, Long> {
}
