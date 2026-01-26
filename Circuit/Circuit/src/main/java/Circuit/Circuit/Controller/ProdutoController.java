package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.Categoria;
import Circuit.Circuit.Model.Produto;
import Circuit.Circuit.Repository.CategoriaRepository;
import Circuit.Circuit.Service.FornecedorService;
import Circuit.Circuit.Service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/estoque")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private FornecedorService fornecedorService;
    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public String abrirEstoque(Model model) {
        List<Produto> ativos = produtoService.listarProdutos();
        List<Produto> inativos = produtoService.listarProdutosInativos();
        List<Categoria>categorias = categoriaRepository.findAll();
        Long produtosCriticos = produtoService.contarCriticos();

        model.addAttribute("listaAtivos", ativos);
        model.addAttribute("listaInativos", inativos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("listaFornecedores", fornecedorService.listarFornecedoresAtivos());
        model.addAttribute("totalCriticos", produtoService.contarCriticos());
        model.addAttribute("tipoEstoque", "produto");
        model.addAttribute("produto", new Produto());

        return "estoque";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute Produto produto, RedirectAttributes redirectAttributes) {
        try {
            produtoService.cadastrar(produto);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto salvo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/estoque";
    }

    @PostMapping("/editar")
    public String editar(@ModelAttribute Produto produto, RedirectAttributes redirectAttributes) {
        try {
            produtoService.editarProduto(produto.getId(), produto);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao editar: " + e.getMessage());
        }
        return "redirect:/estoque";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            produtoService.excluirProduto(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto desativado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/estoque";
    }

    @GetMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            produtoService.restaurarProduto(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto restaurado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao restaurar: " + e.getMessage());
        }
        return "redirect:/estoque";
    }
}
