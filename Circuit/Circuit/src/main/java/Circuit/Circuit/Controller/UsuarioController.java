    package Circuit.Circuit.Controller;

    import Circuit.Circuit.Model.User;
    import Circuit.Circuit.Service.UsuarioService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    import java.util.List;

    @Controller
    @RequestMapping("/usuarios")
    public class UsuarioController {
        @Autowired
        private UsuarioService userService;

        @PostMapping("/cadastrar")
        public String cadastrar(@ModelAttribute User usuario, RedirectAttributes redirectAttributes) {
            try {
                userService.cadastrar(usuario);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário salvo com sucesso!");
            } catch (RuntimeException e) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Erro: " + e.getMessage());
            }
            return "redirect:/usuarios";
        }

        @GetMapping
        public String abrirPaginaUsuarios(Model model) {
            List<User> ativos = userService.ListarUsuarios();
            List<User> inativos = userService.ListarUsuarioInativos();
            model.addAttribute("listaAtivos", ativos);
            model.addAttribute("listaInativos", inativos);
            model.addAttribute("user", new User());
            return "usuarios";
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
