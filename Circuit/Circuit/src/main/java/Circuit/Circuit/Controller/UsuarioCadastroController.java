package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.User;
import Circuit.Circuit.Service.UsuarioCadastroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioCadastroController {
    @Autowired
    private UsuarioCadastroService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrar(@RequestBody User usuarioCadastro){
        try{
            usuarioService.cadastrar(usuarioCadastro);
            return ResponseEntity.ok("Usuário cadastrado com sucesso!");
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
