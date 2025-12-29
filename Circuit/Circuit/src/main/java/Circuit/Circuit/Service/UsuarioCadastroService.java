package Circuit.Circuit.Service;

import Circuit.Circuit.Model.User;
import Circuit.Circuit.Repository.UsuarioCadastroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;

@Service
public class UsuarioCadastroService {
    @Autowired
    private UsuarioCadastroRepository userRepository;
    public User cadastrar(User usuario){
        if (userRepository.existsBycpf(usuario.getCpf())){
            throw new RuntimeException("ERRO: Este CPF já está cadastrado no sistema.");
        }
        usuario.setAtivo(true);
        usuario.setDataCadastro(new Timestamp(System.currentTimeMillis()));
        return userRepository.save(usuario);
    }
}
