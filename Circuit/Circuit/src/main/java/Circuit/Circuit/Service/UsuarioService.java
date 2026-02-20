package Circuit.Circuit.Service;

import Circuit.Circuit.Model.User;
import Circuit.Circuit.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User cadastrar(User usuario) {
        if (userRepository.existsByCpf(usuario.getCpf())) {
            throw new RuntimeException("Este CPF já está cadastrado no sistema.");
        }

        usuario.setDataCadastro(new Timestamp(System.currentTimeMillis()));
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        return userRepository.save(usuario);
    }

    public List<User> ListarUsuarios() {
        return userRepository.findByAtivoTrueOrderById();
    }

    public List<User> ListarUsuarioInativos(){
        return userRepository.findByAtivoFalseOrderById();
    }

    public void excluirUsuarios(Long id) {
        User user = userRepository.getReferenceById(id);
        user.setAtivo(false);
        userRepository.save(user);
    }

    public void restaurarUsuario(Long id){
        User user = userRepository.getReferenceById(id);
        user.setAtivo(true);
        userRepository.save(user);
    }

    public User editarUsuario(Long id, User dadosAtualizados) {
        User usuarioEditar = userRepository.getReferenceById(id);
        usuarioEditar.setNome(dadosAtualizados.getNome());
        usuarioEditar.setCpf(dadosAtualizados.getCpf());
        usuarioEditar.setCargo(dadosAtualizados.getCargo());
        usuarioEditar.setAtivo(dadosAtualizados.getAtivo());

        if (dadosAtualizados.getSenha() != null && !dadosAtualizados.getSenha().isEmpty()) {
            String novaSenhaCriptografada = passwordEncoder.encode(dadosAtualizados.getSenha());
            usuarioEditar.setSenha(novaSenhaCriptografada);
        }

        return userRepository.save(usuarioEditar);
    }
}