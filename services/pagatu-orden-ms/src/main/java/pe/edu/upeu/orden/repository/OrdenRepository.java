package pe.edu.upeu.orden.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.orden.entity.Orden;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

    Optional<Orden> findByCodigo(String codigo);
}
