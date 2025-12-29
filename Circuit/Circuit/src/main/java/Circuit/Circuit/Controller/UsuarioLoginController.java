package Circuit.Circuit.Controller;

import Circuit.Circuit.Service.UsuarioLoginService;
import Circuit.Circuit.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@CrossOrigin(origins = "*")
public class UsuarioLoginController {
    @Autowired
    private UsuarioLoginService Service;
    @PostMapping("/login")
    public ResponseEntity<Object> Logar(@RequestBody User LoginDados){
        User user = Service.login(LoginDados.getCpf(),LoginDados.getSenha());
        if (user != null){
            return ResponseEntity.ok(user);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("CPF ou Senha inválidos");
        }
    }

}
