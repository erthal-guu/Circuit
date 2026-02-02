package Circuit.Circuit.Repository;

import Circuit.Circuit.Dto.ServicoDto;
import Circuit.Circuit.Model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    @Query("SELECT s FROM Servico s LEFT JOIN FETCH s.pecasSugeridas WHERE s.id = :id")
    Servico findByIdComPecas(Long id);
    List<Servico> findByAtivoTrue();
    List<Servico> findByAtivoFalse();

    List<Servico> findByid(Long id);
}
