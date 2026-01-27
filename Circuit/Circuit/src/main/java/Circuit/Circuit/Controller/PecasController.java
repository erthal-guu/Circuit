package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.CategoriaPecas;
import Circuit.Circuit.Model.Pecas;
import Circuit.Circuit.Repository.CategoriaPecasRepository;
import Circuit.Circuit.Service.FornecedorService;
import Circuit.Circuit.Service.PecasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/estoquePecas")
public class PecasController {

    @Autowired
    private PecasService pecasService;
    @Autowired
    private CategoriaPecasRepository categoriasRepository;
    @Autowired
    private FornecedorService fornecedorService;

    @GetMapping
    public String abrirPecas(Model model) {
        List<Pecas> ativos = pecasService.listarPecasAtivas();
        List<Pecas> inativos = pecasService.listarPecasInativas();
        List<CategoriaPecas> categorias =categoriasRepository.findAllByOrderByTipoAsc();
        BigDecimal valorTotalPecas = pecasService.valorTotalPecas();


        model.addAttribute("listaAtivos", ativos);
        model.addAttribute("listaInativos", inativos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("listaFornecedores", fornecedorService.listarFornecedoresAtivos());
        model.addAttribute("totalCriticos", pecasService.contarCriticos());
        model.addAttribute("valorTotalPecas", valorTotalPecas);
        model.addAttribute("tipoEstoque", "peca");
        model.addAttribute("peca", new Pecas());
        return "estoquePecas";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute Pecas pecas, RedirectAttributes redirectAttributes) {
        try {
            pecasService.cadastrar(pecas);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Peça salva com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/estoquePecas";
    }

    @PostMapping("/editar")
    public String editar(@ModelAttribute Pecas pecas, RedirectAttributes redirectAttributes) {
        try {
            pecasService.editarPeca(pecas.getId(), pecas);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Peça atualizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao editar: " + e.getMessage());
        }
        return "redirect:/estoquePecas";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pecasService.excluirPeca(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Peça desativada!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao desativar: " + e.getMessage());
        }
        return "redirect:/estoquePecas";
    }

    @GetMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pecasService.restaurarPeca(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Peça restaurada!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao restaurar: " + e.getMessage());
        }
        return "redirect:/estoquePecas";
    }
}
