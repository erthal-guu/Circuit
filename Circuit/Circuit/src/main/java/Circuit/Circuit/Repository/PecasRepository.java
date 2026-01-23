package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Pecas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PecasRepository extends JpaRepository<Pecas,Long> {
    List<Pecas> findByAtivoTrueOrderById();
    List<Pecas> findByAtivoFalseOrderById();
    @Query("SELECT COUNT(p) FROM Produto p WHERE p.quantidade <= p.quantidadeMinima AND p.ativo = true")
    long countItensCriticos();
}
