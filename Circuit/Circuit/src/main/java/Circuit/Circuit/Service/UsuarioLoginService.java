package Circuit.Circuit.Service;

import Circuit.Circuit.Model.User;
import Circuit.Circuit.Repository.UsuarioLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class UsuarioLoginService {
    @Autowired
    private UsuarioLoginRepository Repository;

    public User login (String cpf, String Senha){
        User user = Repository.findByCpf(cpf);
        if (user != null && user.getAtivo() && user.getSenha().equals(Senha)){
            return user;
        }
        return null;
    }
}
