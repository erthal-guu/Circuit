package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.User;
import Circuit.Circuit.Repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private LoginRepository loginRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/api/me")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUsuarioLogado(Authentication authentication) {
        User user = loginRepository.findByCpf(authentication.getName());
        return ResponseEntity.ok(Map.of(
                "nome", user.getNome(),
                "cargo", user.getCargo().toString()
        ));
    }
}
