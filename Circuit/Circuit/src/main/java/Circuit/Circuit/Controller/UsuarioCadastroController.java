package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.User;
import Circuit.Circuit.Service.UsuarioCadastroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioCadastroController {
    @Autowired
    private UsuarioCadastroService userService;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrar(@RequestBody User usuarioCadastro){
        try{
            userService.cadastrar(usuarioCadastro);
            return ResponseEntity.ok("Usuário cadastrado com sucesso!");
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/listar")
    public List<User> listarUsuarios(){
        return userService.ListarUsuarios();
    }
    @DeleteMapping("/excluir/{id}")
    public void excluir (@PathVariable Long id){
        userService.excluirUsuarios(id);
    }
    @PutMapping("/editar/{id}")
    public User editarUsuario(@PathVariable Long id,@RequestBody User usuario){
        return  userService.editarUsuario(id,usuario);
    }
}
