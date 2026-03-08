package Circuit.Circuit.Service;

import Circuit.Circuit.Model.User;
import Circuit.Circuit.Repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User cadastrar(User usuario) {
        logger.info("Tentativa de cadastro de novo usuário - Nome: {}, CPF: {}, Cargo: {}",
                usuario.getNome(), usuario.getCpf(), usuario.getCargo());

        if (userRepository.existsByCpf(usuario.getCpf())) {
            logger.warn("Falha ao cadastrar usuário: CPF já existe - {}", usuario.getCpf());
            throw new RuntimeException("Este CPF já está cadastrado no sistema.");
        }

        usuario.setDataCadastro(new Timestamp(System.currentTimeMillis()));
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        User usuarioSalvo = userRepository.save(usuario);
        logger.info("Usuário cadastrado com sucesso - ID: {}, Nome: {}, CPF: {}, Cargo: {}",
                usuarioSalvo.getId(), usuarioSalvo.getNome(), usuarioSalvo.getCpf(), usuarioSalvo.getCargo());

        return usuarioSalvo;
    }

    public List<User> ListarUsuarios() {
        return userRepository.findByAtivoTrueOrderById();
    }

    public List<User> ListarUsuarioInativos(){
        return userRepository.findByAtivoFalseOrderById();
    }

    public void excluirUsuarios(Long id) {
        User user = userRepository.getReferenceById(id);
        logger.info("Tentativa de exclusão de usuário - ID: {}, Nome: {}, CPF: {}",
                user.getId(), user.getNome(), user.getCpf());

        user.setAtivo(false);
        userRepository.save(user);

        logger.info("Usuário desativado com sucesso - ID: {}, Nome: {}, CPF: {}",
                user.getId(), user.getNome(), user.getCpf());
    }

    public void restaurarUsuario(Long id){
        User user = userRepository.getReferenceById(id);
        logger.info("Tentativa de restauração de usuário - ID: {}, Nome: {}, CPF: {}",
                user.getId(), user.getNome(), user.getCpf());

        user.setAtivo(true);
        userRepository.save(user);

        logger.info("Usuário restaurado com sucesso - ID: {}, Nome: {}, CPF: {}",
                user.getId(), user.getNome(), user.getCpf());
    }

    public User editarUsuario(Long id, User dadosAtualizados) {
        User usuarioEditar = userRepository.getReferenceById(id);
        logger.info("Tentativa de edição de usuário - ID: {}, Nome atual: {}, Nome novo: {}, Cargo novo: {}",
                id, usuarioEditar.getNome(), dadosAtualizados.getNome(), dadosAtualizados.getCargo());

        usuarioEditar.setNome(dadosAtualizados.getNome());
        usuarioEditar.setCpf(dadosAtualizados.getCpf());
        usuarioEditar.setCargo(dadosAtualizados.getCargo());
        usuarioEditar.setAtivo(dadosAtualizados.getAtivo());

        boolean senhaAlterada = false;
        if (dadosAtualizados.getSenha() != null && !dadosAtualizados.getSenha().isEmpty()) {
            String novaSenhaCriptografada = passwordEncoder.encode(dadosAtualizados.getSenha());
            usuarioEditar.setSenha(novaSenhaCriptografada);
            senhaAlterada = true;
        }

        User usuarioSalvo = userRepository.save(usuarioEditar);
        logger.info("Usuário editado com sucesso - ID: {}, Nome: {}, CPF: {}, Cargo: {}, Senha alterada: {}",
                usuarioSalvo.getId(), usuarioSalvo.getNome(), usuarioSalvo.getCpf(),
                usuarioSalvo.getCargo(), senhaAlterada);

        return usuarioSalvo;
    }
}