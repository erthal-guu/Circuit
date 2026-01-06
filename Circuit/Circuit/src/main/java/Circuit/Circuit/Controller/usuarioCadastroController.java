    package Circuit.Circuit.Controller;

    import Circuit.Circuit.Model.User;
    import Circuit.Circuit.Service.usuarioCadastroService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/usuarios")
    public class usuarioCadastroController {
        @Autowired
        private usuarioCadastroService userService;

        @PostMapping("/cadastrar")
        public ResponseEntity<String> cadastrar(@RequestBody User usuarioCadastro) {
            try {
                userService.cadastrar(usuarioCadastro);
                return ResponseEntity.ok("Usuário cadastrado com sucesso!");
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        @GetMapping("/listar-ativos")
        public List<User> listarUsuarios() {
            return userService.ListarUsuarios();
        }

        @GetMapping("/listar-inativos")
        public List<User> listarUsuariosInativos() {
            return userService.ListarUsuarioInativos();
        }

        @DeleteMapping("/excluir/{id}")
        public void excluir(@PathVariable Long id) {
            userService.excluirUsuarios(id);
        }

        @PutMapping("/editar/{id}")
        public User editarUsuario(@PathVariable Long id, @RequestBody User usuario) {
            return userService.editarUsuario(id, usuario);
        }
        @PutMapping("/restaurar/{id}")
        public void restaurar (@PathVariable Long id) {
             userService.restaurarUsuario(id);
        }
        @GetMapping("/pesquisar-ativos")
        public List<User> pesquisarAtivos(@RequestParam("nome") String nome){
            return userService.pesquisarAtivos(nome);
        }
        @GetMapping("/pesquisar-inativos")
        public List<User> pesquisarInativos(@RequestParam("nome") String nome){
            return userService.pesquisarInativo(nome);
        }
    }
