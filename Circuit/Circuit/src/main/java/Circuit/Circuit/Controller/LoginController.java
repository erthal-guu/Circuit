package Circuit.Circuit.Controller;

import Circuit.Circuit.Service.LoginService;
import Circuit.Circuit.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {
    @Autowired
    private LoginService service;


    @PostMapping("/login")
    public ResponseEntity<?> logar(@RequestBody User loginDados) {
        try {
            User user = service.login(loginDados.getCpf(), loginDados.getSenha());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}

