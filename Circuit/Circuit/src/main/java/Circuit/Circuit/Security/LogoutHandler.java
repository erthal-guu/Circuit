package Circuit.Circuit.Security;

import Circuit.Circuit.Model.User;
import Circuit.Circuit.Repository.LoginRepository;
import Circuit.Circuit.Service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LogoutHandler implements LogoutSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private LoginService loginService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                              Authentication authentication) {
        if (authentication != null && authentication.getName() != null) {
            String cpf = authentication.getName();
            User user = loginRepository.findByCpf(cpf);

            if (user != null) {
                logger.info("Logout realizado - CPF: {}, Nome: {}", cpf, user.getNome());
                loginService.logLogout(cpf, user.getNome());
            } else {
                logger.warn("Logout realizado mas usuário não encontrado - CPF: {}", cpf);
            }
        } else {
            logger.info("Logout realizado sem autenticação ativa");
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
