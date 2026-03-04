package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.ContasReceber;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContaReceberRepository extends JpaRepository<ContasReceber, Long> {
     boolean existsByOrigemAndOrigemId(String origem, Long origemId);
     List<ContasReceber> findByStatus(StatusFinanceiro status);
}
