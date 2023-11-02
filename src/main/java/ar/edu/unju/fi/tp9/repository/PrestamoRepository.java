package ar.edu.unju.fi.tp9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.tp9.entity.Prestamo;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long>{
}
