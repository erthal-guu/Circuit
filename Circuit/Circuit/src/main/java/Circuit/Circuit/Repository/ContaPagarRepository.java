package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.ContasPagar;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContaPagarRepository extends JpaRepository<ContasPagar, Long> {
    List<ContasPagar> findByStatus(StatusFinanceiro status);
}
