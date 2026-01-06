package Circuit.Circuit.Repository;
import Circuit.Circuit.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface usuarioLoginRepository extends JpaRepository<User,Long> {
     User findByCpf(String cpf);
}
