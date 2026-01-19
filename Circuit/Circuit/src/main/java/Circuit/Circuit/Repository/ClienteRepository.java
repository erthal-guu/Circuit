package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Cliente;
import Circuit.Circuit.Model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCpf(String cpf);
    List<Cliente> findByAtivoTrue();
    List<Cliente> findByAtivoFalse();
}
