package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    boolean existsByCnpj(String cnpj);

    List<Fornecedor> findByAtivoTrueOrderById();

    List<Fornecedor> findByAtivoFalseOrderById();

    @Query("SELECT f FROM Fornecedor f WHERE LOWER(f.nomeFantasia) LIKE LOWER(CONCAT('%', :nome, '%')) AND f.ativo = true")
    List<Fornecedor> findByNomeFantasiaContainingIgnoreCaseAndAtivoTrue(@Param("nome") String nome);

    @Query("SELECT f FROM Fornecedor f WHERE LOWER(f.nomeFantasia) LIKE LOWER(CONCAT('%', :nome, '%')) AND f.ativo = false")
    List<Fornecedor> findByNomeFantasiaContainingIgnoreCaseAndAtivoFalse(@Param("nome") String nome);
}