package Circuit.Circuit.Service;

import Circuit.Circuit.Model.User;
import Circuit.Circuit.Repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private LoginRepository Repository;

    public User login (String cpf, String Senha){
        User user = Repository.findByCpf(cpf);
        if (user != null && user.getAtivo() && user.getSenha().equals(Senha)){
            return user;
        }
        return null;
    }
}
