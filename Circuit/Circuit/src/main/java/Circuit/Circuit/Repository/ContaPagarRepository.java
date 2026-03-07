package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.ContasPagar;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContaPagarRepository extends JpaRepository<ContasPagar, Long> {
    List<ContasPagar> findByStatus(StatusFinanceiro status);
    boolean existsByOrigemAndOrigemId(String origem, Long origemId);

    @Query("SELECT COALESCE(c.origem, 'Outros') as categoria, COALESCE(SUM(c.valor), 0) as total " +
           "FROM ContasPagar c " +
           "WHERE c.status = 'PAGO' " +
           "GROUP BY c.origem " +
           "ORDER BY total DESC")
    List<Object[]> buscarDistribuicaoDespesas();

    @Query("SELECT SUM(c.valor) FROM ContasPagar c WHERE c.status = 'PAGO' AND c.dataPagamento >= :dataInicio AND c.dataPagamento <= :dataFim")
    BigDecimal buscarTotalDespesasPorPeriodo(LocalDate dataInicio, LocalDate dataFim);

    @Query("SELECT SUM(c.valor) FROM ContasPagar c WHERE c.status = 'PAGO' AND FUNCTION('MONTH', c.dataPagamento) = :mes")
    BigDecimal buscarTotalDespesasPorMes(Integer mes);

    @Query("SELECT c FROM ContasPagar c WHERE c.dataVencimento BETWEEN :dataInicio AND :dataFim AND c.status = 'PENDENTE' ORDER BY c.dataVencimento ASC")
    List<ContasPagar> buscarContasPagarProximos30Dias(LocalDate dataInicio, LocalDate dataFim);


    @Query("SELECT SUM(c.valor) FROM ContasPagar c WHERE c.dataVencimento BETWEEN :dataInicio AND :dataFim AND c.status = 'PENDENTE'")
    BigDecimal buscarTotalContasPagarProximos30Dias(LocalDate dataInicio, LocalDate dataFim);
}
