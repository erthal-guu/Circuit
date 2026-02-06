package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.Peca;
import Circuit.Circuit.Model.Produto;
import Circuit.Circuit.Model.viaCep;
import Circuit.Circuit.Model.Fornecedor;
import Circuit.Circuit.Service.CepService;
import Circuit.Circuit.Service.FornecedorService;
import Circuit.Circuit.Service.PecaService;
import Circuit.Circuit.Service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private CepService viaCepService;

    @Autowired
    private PecaService pecaService;

    @Autowired
    private ProdutoService produtoService;

    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute Fornecedor fornecedor, RedirectAttributes redirectAttributes) {
        try {
            fornecedorService.cadastrar(fornecedor);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Fornecedor salvo com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro: " + e.getMessage());
        }
        return "redirect:/fornecedores";
    }

    @GetMapping("/consulta-cep-fornecedor/{cep}")
    @ResponseBody
    public ResponseEntity<?> consultarCep(@PathVariable String cep) {
        try {
            viaCep dadosEndereco = viaCepService.consultarCep(cep);
            return ResponseEntity.ok(dadosEndereco);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping
    public String listarFuncionarios(Model model){
        List<Fornecedor> ativos = fornecedorService.listarFornecedoresAtivos();
        List<Fornecedor> Inativos = fornecedorService.listarFornecedoresInativos();
        List<Peca> pecas = pecaService.listarPecasAtivas();
        List<Produto> produtos = produtoService.listarProdutos();
        model.addAttribute("listaAtivos", ativos);
        model.addAttribute("listaInativos", Inativos);
        model.addAttribute("listaPecas", pecas);
        model.addAttribute("listaProdutos", produtos);
        model.addAttribute("fornecedores", new Fornecedor());
        return "fornecedores";
    }

    @GetMapping("/excluir/{id}")
    public String excluirFornecedor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            fornecedorService.excluirFornecedor(id);
            redirectAttributes.addFlashAttribute("mensagemDelete", "Funcionário desativado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/fornecedores";
    }

    @GetMapping("/restaurar/{id}")
    public String  restaurarFornecedor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            fornecedorService.restaurarFornecedor(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso","Fornecedor restaurado com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao restaurar: " + e.getMessage());
        }
        return "redirect:/fornecedores";
    }

    @PostMapping("/editar")
    public String editarFornecedor(@ModelAttribute Fornecedor fornecedor,RedirectAttributes redirectAttributes ) {
        try {
            fornecedorService.editarFornecedor(fornecedor.getId(),fornecedor);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Fornecedor editado com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao editar: " + e.getMessage());
        }
        return "redirect:/fornecedores";
    }
    @PostMapping("/vincular")
    public String vincularLote(@RequestParam Long fornecedorId,
                               @RequestParam List<Long> itensIds,
                               @RequestParam String tipo,
                               RedirectAttributes ra) {
        try {
            fornecedorService.vincularItens(fornecedorId, itensIds, tipo);
            ra.addFlashAttribute("mensagemSucesso", "Itens vinculados com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagemErro", "Erro ao processar vínculo: " + e.getMessage());
        }
        return "redirect:/fornecedores";
    }
}