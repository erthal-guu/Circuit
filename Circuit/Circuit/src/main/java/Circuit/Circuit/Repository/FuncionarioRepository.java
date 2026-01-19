package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    boolean existsByCpf(String cpf);

    List<Funcionario> findByAtivoTrueOrderById();

    List<Funcionario> findByAtivoFalseOrderById();

}
