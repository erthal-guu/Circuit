package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioCadastroRepository extends JpaRepository<User,Long> {
    User findByCpf(String cpf);
    boolean existsBycpf(String cpf);
}
