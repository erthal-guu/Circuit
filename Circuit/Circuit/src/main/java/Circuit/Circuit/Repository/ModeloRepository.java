package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    List<Modelo> findByAtivoTrueOrderById();
    List<Modelo> findByAtivoFalseOrderById();
}
