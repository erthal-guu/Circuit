package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface funcionarioCadastroRepository extends JpaRepository<Funcionario,Long> {
    boolean existsByCpf(String cpf);
    List<Funcionario> findByAtivoTrue();
    List<Funcionario> findByAtivoFalse();

}
