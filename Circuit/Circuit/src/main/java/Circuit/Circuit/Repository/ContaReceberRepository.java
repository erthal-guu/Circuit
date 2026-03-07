package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.ContasReceber;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContaReceberRepository extends JpaRepository<ContasReceber, Long> {
     boolean existsByOrigemAndOrigemId(String origem, Long origemId);
     List<ContasReceber> findByStatus(StatusFinanceiro status);

     @Query("SELECT SUM(c.valor) FROM ContasReceber c WHERE c.status = 'PAGO' AND c.dataPagamento >= :dataInicio AND c.dataPagamento <= :dataFim")
     BigDecimal buscarTotalReceitasPorPeriodo(LocalDate dataInicio, LocalDate dataFim);

     @Query("SELECT SUM(c.valor) FROM ContasReceber c WHERE c.status = 'PAGO' AND FUNCTION('MONTH', c.dataPagamento) = :mes")
     BigDecimal buscarTotalReceitasPorMes(Integer mes);

     @Query("SELECT c FROM ContasReceber c WHERE c.dataVencimento BETWEEN :dataInicio AND :dataFim AND c.status = 'PENDENTE' ORDER BY c.dataVencimento ASC")
     List<ContasReceber> buscarContasReceberProximos30Dias(LocalDate dataInicio, LocalDate dataFim);



     @Query("SELECT SUM(c.valor) FROM ContasReceber c WHERE c.dataVencimento BETWEEN :dataInicio AND :dataFim AND c.status = 'PENDENTE'")
     BigDecimal buscarTotalContasReceberProximos30Dias(LocalDate dataInicio, LocalDate dataFim);
}
