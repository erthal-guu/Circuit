package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.ContasReceber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardRepository extends JpaRepository<ContasReceber, Long> {
    @Query("SELECT SUM (c.valor) FROM ContasReceber c WHERE c.status = 'PAGO'")
    Double calcularReceitaTotal();
    @Query("SELECT SUM (c.valor) FROM ContasPagar c WHERE c.status = 'PAGO'")
    Double calcularDespesaTotal();
}
