package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Pedido;
import Circuit.Circuit.Model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByStatusIn(List<StatusPedido> status);

}
