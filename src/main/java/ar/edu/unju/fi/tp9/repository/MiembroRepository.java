package ar.edu.unju.fi.tp9.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.tp9.entity.Miembro;




@Repository
public interface MiembroRepository extends CrudRepository<Miembro, Integer>{
    
    public Optional<Miembro> findByNombre(String nombre);

    public Optional<Miembro> findByCorreo(String correo);

    public void deleteById(Integer id);

    
}
