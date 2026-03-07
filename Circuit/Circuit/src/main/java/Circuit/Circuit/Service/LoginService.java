package Circuit.Circuit.Service;

import Circuit.Circuit.Model.User;
import Circuit.Circuit.Repository.LoginRepository;
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

    @Autowired
    private LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        User user = loginRepository.findByCpf(cpf);

        if (user == null) {
            throw new UsernameNotFoundException("CPF ou Senha inválidos");
        }

        if (!user.getAtivo()) {
            throw new DisabledException("Acesso negado: Usuário inativo.");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getCpf())
                .password(user.getSenha())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getCargo().name())))
                .build();
    }
}