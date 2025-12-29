package Circuit.Circuit.Repository;
import Circuit.Circuit.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioLoginRepository  extends JpaRepository<User,Long> {
     User findByCpf(String cpf);
}
