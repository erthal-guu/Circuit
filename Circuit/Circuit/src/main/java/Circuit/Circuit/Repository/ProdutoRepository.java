package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByAtivoTrueOrderById();
    List<Produto> findByAtivoFalseOrderById();
    List<Produto> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);
    List<Produto> findByNomeContainingIgnoreCaseAndAtivoFalse(String nome);
}