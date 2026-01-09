package Circuit.Circuit.Service;

import Circuit.Circuit.Model.User;
import Circuit.Circuit.Repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private LoginRepository Repository;

    public User login(String cpf, String senha) {
        User user = Repository.findByCpf(cpf);

        if (user == null) {
            throw new RuntimeException("CPF ou Senha inválidos");
        }
        if (!user.getSenha().equals(senha)) {
            throw new RuntimeException("CPF ou Senha inválidos");
        }
        if (!user.getAtivo()) {
            throw new RuntimeException("Acesso negado: Usuário inativo. Fale com o suporte.");
        }

        return user;
    }
}