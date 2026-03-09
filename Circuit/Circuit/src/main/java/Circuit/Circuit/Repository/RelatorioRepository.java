package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RelatorioRepository extends JpaRepository<Venda, Long> {

    @Query(value = "SELECT f.nome, SUM(v.valor_total) as total_vendido, COUNT(v.id) as qtd_vendas " +
            "FROM funcionarios f " +
            "INNER JOIN vendas v ON v.funcionario_id = f.id " +
            "WHERE v.data_venda::date BETWEEN :inicio AND :fim " +
            "GROUP BY f.id, f.nome " +
            "ORDER BY total_vendido DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> findTopFuncionarios(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query(value = "SELECT p.nome, SUM(iv.quantidade) as total_utilizado " +
            "FROM produtos p " +
            "INNER JOIN itens_venda iv ON iv.produto_id = p.id " +
            "INNER JOIN vendas v ON iv.venda_id = v.id " +
            "WHERE v.data_venda::date BETWEEN :inicio AND :fim " +
            "GROUP BY p.id, p.nome " +
            "ORDER BY total_utilizado DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> findProdutosMaisUtilizados(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query(value = "SELECT c.nome, SUM(cr.valor) as total_a_receber " +
            "FROM Clientes c " +
            "INNER JOIN contas_a_receber cr ON cr.cliente_id = c.id " +
            "WHERE cr.data_vencimento::date BETWEEN :inicio AND :fim " +
            "GROUP BY c.id, c.nome " +
            "ORDER BY total_a_receber DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> findClientesMaisComprasFinanceiro(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}