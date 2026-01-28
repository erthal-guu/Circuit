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
}