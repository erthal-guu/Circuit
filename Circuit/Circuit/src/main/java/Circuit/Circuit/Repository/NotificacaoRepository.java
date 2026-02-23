package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    @Query("SELECT n FROM Notificacao n JOIN FETCH n.pedido WHERE n.tipo = :tipo AND n.lida = false ORDER BY n.dataCriacao DESC")
    List<Notificacao> findPendentesByTipo(@Param("tipo") String tipo);
}