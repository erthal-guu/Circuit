package Circuit.Circuit.Controller;

import Circuit.Circuit.Dto.PecaDto;
import Circuit.Circuit.Model.CategoriaPecas;
import Circuit.Circuit.Model.Peca;
import Circuit.Circuit.Repository.CategoriaPecasRepository;
import Circuit.Circuit.Service.FornecedorService;
import Circuit.Circuit.Service.PecaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/estoquePecas")
public class PecaController {

    @Autowired
    private PecaService pecasService;
    @Autowired
    private CategoriaPecasRepository categoriasRepository;
    @Autowired
    private FornecedorService fornecedorService;

    @GetMapping
    public String abrirPecas(Model model) {
        List<Peca> ativos = pecasService.listarPecasAtivas();
        List<Peca> inativos = pecasService.listarPecasInativas();
        List<CategoriaPecas> categorias = categoriasRepository.findAllByOrderByTipoAsc();
        BigDecimal valorTotalPecas = pecasService.valorTotalPecas();


        model.addAttribute("listaAtivos", ativos);
        model.addAttribute("listaInativos", inativos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("listaFornecedores", fornecedorService.listarFornecedoresAtivos());
        model.addAttribute("totalCriticas", pecasService.contarCriticos());
        model.addAttribute("valorTotalPecas", valorTotalPecas);
        model.addAttribute("tipoEstoque", "peca");
        model.addAttribute("peca", new Peca());
        return "estoquePecas";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute Peca pecas, RedirectAttributes redirectAttributes) {
        try {
            pecasService.cadastrar(pecas);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Peça salva com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/estoquePecas";
    }

    @PostMapping("/editar")
    public String editar(@ModelAttribute Peca pecas, RedirectAttributes redirectAttributes) {
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
            redirectAttributes.addFlashAttribute("mensagemDelete", "Peça desativada!");
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
    @GetMapping("/todas-disponiveis")
    @ResponseBody
    public List<Map<String, Object>> listarPecasJson() {
        List<Peca> pecas = pecasService.listarPecasAtivas();
        return pecas.stream()
                .map(peca -> Map.<String, Object>of(
                        "id", peca.getId(),
                        "nome", peca.getNome(),
                        "quantidade", peca.getQuantidade()
                ))
                .collect(Collectors.toList());
    }

}
