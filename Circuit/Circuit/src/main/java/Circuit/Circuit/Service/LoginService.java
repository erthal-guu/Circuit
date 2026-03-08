package Circuit.Circuit.Service;

import Circuit.Circuit.Model.User;
import Circuit.Circuit.Repository.LoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class LoginService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        logger.info("Tentativa de login para CPF: {}", cpf);

        User user = loginRepository.findByCpf(cpf);

        if (user == null) {
            logger.warn("Tentativa de login falhou: CPF não encontrado - {}", cpf);
            throw new UsernameNotFoundException("CPF ou Senha inválidos");
        }

        if (!user.getAtivo()) {
            logger.warn("Tentativa de login falhou: Usuário inativo - CPF: {}, Nome: {}", cpf, user.getNome());
            throw new DisabledException("Acesso negado: Usuário inativo.");
        }

        logger.info("Login realizado com sucesso - CPF: {}, Nome: {}, Cargo: {}", cpf, user.getNome(), user.getCargo());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getCpf())
                .password(user.getSenha())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getCargo().name())))
                .build();
    }

    public void logLogout(String cpf, String nome) {
        logger.info("Logout realizado - CPF: {}, Nome: {}", cpf, nome);
    }
}