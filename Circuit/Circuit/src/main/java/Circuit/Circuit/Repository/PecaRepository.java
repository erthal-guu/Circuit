package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Peca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface PecaRepository extends JpaRepository<Peca,Long> {
    List<Peca> findByAtivoTrueOrderById();
    List<Peca> findByAtivoFalseOrderById();
    @Query("SELECT COUNT(p) FROM Peca p WHERE p.quantidade <= p.quantidadeMinima AND p.ativo = true")
    long countItensCriticos();

    @Query("SELECT SUM(p.precoVenda) FROM Peca p")
    BigDecimal sumPrecoVenda();

}
