package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Aparelho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AparelhoRepository extends JpaRepository<Aparelho, Long> {
    List<Aparelho> findByAtivoTrueOrderById();
    List<Aparelho> findByAtivoFalseOrderById();
    List<Aparelho> findByClienteIdAndAtivoTrue(Long clienteId);
}
