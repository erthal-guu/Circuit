package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<User,Long> {
    boolean existsByCpf(String cpf);
    List<User> findByAtivoTrueOrderById();
    List<User> findByAtivoFalseOrderById();
}