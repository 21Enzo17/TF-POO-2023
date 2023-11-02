package ar.edu.unju.fi.tp9.repository;

import org.springframework.stereotype.Repository;
import ar.edu.unju.fi.tp9.entity.Prestamo;

@Repository
public interface PrestamoRepository extends CrudRepository<Prestamo, Integer> {
    public Prestamo findByMiembro(Miembro miembro);
}
