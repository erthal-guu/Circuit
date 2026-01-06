package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Funcionario;
import Circuit.Circuit.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface funcionarioCadastroRepository extends JpaRepository<Funcionario,Long> {
    boolean existsByCpf(String cpf);
    List<Funcionario> findByAtivoTrue();
    List<Funcionario> findByAtivoFalse();
    @Query("SELECT f FROM Funcionario f WHERE LOWER(f.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND f.ativo = true")
    List<Funcionario> findByNomeContainingIgnoreCaseAndAtivoTrue(@Param("nome") String nome);
    @Query("SELECT f FROM Funcionario f WHERE LOWER(f.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND f.ativo = false")
    List<Funcionario> findByNomeContainingIgnoreCaseAndAtivoFalse(@Param("nome") String nome);

}
