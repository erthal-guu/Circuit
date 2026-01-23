package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Pecas;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PecasRepository {
    List<Pecas> findByAtivoTrueOrderById();
    List<Pecas> findByAtivoFalseOrderById();
    @Query("SELECT COUNT(p) FROM Produto p WHERE p.quantidade <= p.quantidadeMinima AND p.ativo = true")
    long countItensCriticos();
}
