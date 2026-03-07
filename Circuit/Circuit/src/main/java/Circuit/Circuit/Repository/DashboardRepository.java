package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.ContasReceber;
import Circuit.Circuit.Model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<ContasReceber, Long> {
    @Query("SELECT SUM (c.valor) FROM ContasReceber c WHERE c.status = 'PAGO'")
    Double calcularReceitaTotal();
    @Query("SELECT SUM (c.valor) FROM ContasPagar c WHERE c.status = 'PAGO'")
    Double calcularDespesaTotal();
    @Query("SELECT SUM(p.quantidade * p.precoCompra) FROM Produto p WHERE p.quantidade > 0")
    Double somaTotalEstoque();
}
