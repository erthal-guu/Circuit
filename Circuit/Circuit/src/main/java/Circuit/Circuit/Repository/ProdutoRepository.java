package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByAtivoTrueOrderById();
    List<Produto> findByAtivoFalseOrderById();
    @Query("SELECT COUNT(p) FROM Produto p WHERE p.quantidade <= p.quantidadeMinima AND p.ativo = true")
    long countItensCriticos();
    @Query("SELECT SUM(p.precoVenda) FROM Produto p")
    BigDecimal sumPrecoVenda();
}