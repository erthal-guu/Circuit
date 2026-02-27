package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda,Long> {

}
