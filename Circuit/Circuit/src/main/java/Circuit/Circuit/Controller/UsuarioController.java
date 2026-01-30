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
        public String listarUsuarios(Model model) {
            List<User> ativos = userService.ListarUsuarios();
            List<User> inativos = userService.ListarUsuarioInativos();
            model.addAttribute("listaAtivos", ativos);
            model.addAttribute("listaInativos", inativos);
            model.addAttribute("user", new User());
            return "usuarios";
        }

        @GetMapping("/excluir/{id}")

        public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
            try {
                userService.excluirUsuarios(id);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário desativado com sucesso.");
            }catch (Exception e){
                redirectAttributes.addFlashAttribute("mensagemErro", "Erro: " + e.getMessage());
            }
            return "redirect:/usuarios";
        }

        @PostMapping("/editar")
        public String editar(@ModelAttribute User usuario, RedirectAttributes redirectAttributes) {
            try {
                userService.editarUsuario(usuario.getId(), usuario);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário atualizado com sucesso!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao editar: " + e.getMessage());
            }
            return "redirect:/usuarios";
        }
        @GetMapping("/restaurar/{id}")
        public String restaurar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
            userService.restaurarUsuario(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário restaurado com sucesso.");
            return "redirect:/usuarios";
        }
    }
