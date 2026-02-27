package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.StatusVenda;
import Circuit.Circuit.Model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByStatus(StatusVenda status);

}
