package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.OrdemServico;
import Circuit.Circuit.Model.Enum.StatusOrdem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
    List<OrdemServico> findByStatusIn(List<StatusOrdem> listaDeStatus);
}
