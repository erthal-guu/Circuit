package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.Aparelho;
import Circuit.Circuit.Model.Cliente;
import Circuit.Circuit.Model.Modelo;
import Circuit.Circuit.Service.AparelhoService;
import Circuit.Circuit.Service.ClienteService;
import Circuit.Circuit.Service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/aparelhos")
public class AparelhoController {
    @Autowired
    private AparelhoService aparelhoService;

    @Autowired
    private ModeloService modeloService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String abrirAparelhos(Model model){
        List<Aparelho> ativos = aparelhoService.listarAparelhosAtivos();
        List<Aparelho> inativos = aparelhoService.listarAparelhosInativos();
        List<Modelo> modelos = modeloService.ListarModelosAtivos();
        List<Cliente> clientes = clienteService.listarAtivos();

        model.addAttribute("listaAtivos", ativos);
        model.addAttribute("listaInativos", inativos);
        model.addAttribute("listaModelos", modelos);
        model.addAttribute("listaClientes", clientes);
        return "aparelhos";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute Aparelho aparelho, RedirectAttributes redirectAttributes) {
        try {
            aparelhoService.cadastrar(aparelho);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Aparelho salvo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/aparelhos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            aparelhoService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Aparelho desativado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao desativar: " + e.getMessage());
        }
        return "redirect:/aparelhos";
    }

    @GetMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            aparelhoService.restaurar(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Aparelho restaurado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao restaurar: " + e.getMessage());
        }
        return "redirect:/aparelhos";
    }
}