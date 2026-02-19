package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.*;
import Circuit.Circuit.Repository.PedidoRepository;
import Circuit.Circuit.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private PecaService pecaService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping
    public String abrirPedido(Model model){
        List<Fornecedor> fornecedores = fornecedorService.listarFornecedoresAtivos();
        List<Funcionario> funcionarios = funcionarioService.listarFuncionarioAtivos();
        List<Pedido> pedidos = pedidoService.listarPedidos();
        List<Produto> produtos = produtoService.listarProdutos();
        List<Peca> pecas = pecaService.listarPecasAtivas();
        model.addAttribute("listaFornecedores",fornecedores);
        model.addAttribute("listaFuncionarios",funcionarios);
        model.addAttribute("listaProdutos", produtos);
        model.addAttribute("listaPecas", pecas);
        model.addAttribute("listaPedidos",pedidos);
        model.addAttribute("pedido", new Pedido());

        return "pedidos";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam(required = false) Long id,
                         @RequestParam Long fornecedorId,
                         @RequestParam Long responsavelId,
                         @RequestParam String numeroPedido,
                         @RequestParam BigDecimal valorTotal,
                         @RequestParam(required = false) String observacao,
                         @RequestParam String tipoPedido,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataPedido,
                         @RequestParam(required = false) List<Long> itensId,
                         @RequestParam(required = false) List<Integer> quantidadeItens,
                         @RequestParam(required = false) List<BigDecimal> precoItens,
                         @RequestParam(required = false, defaultValue = "false") Boolean notificarFornecedor,
                         RedirectAttributes redirectAttributes) {

        if (itensId == null || itensId.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Adicione pelo menos um item ao pedido!");
            return "redirect:/pedidos";
        }

        try {
            pedidoService.salvarPedido(id, fornecedorId, responsavelId, numeroPedido,
                    observacao, tipoPedido, dataPedido, valorTotal,
                    itensId, quantidadeItens, precoItens, notificarFornecedor);

            if (id != null) {
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Pedido atualizado com sucesso.");
            } else {
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Pedido criado com sucesso.");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar pedido: " + e.getMessage());
        }

        return "redirect:/pedidos";
    }

    @PostMapping("/atualizar-status")
    public String atualizarStatus(@RequestParam Long pedidoId,
                                  @RequestParam StatusPedido novoStatus,
                                  @RequestParam(required = false, defaultValue = "false") Boolean notificarFornecedor,
                                  RedirectAttributes ra) {
        try {
            pedidoService.atualizarStatus(pedidoId, novoStatus,notificarFornecedor);
            ra.addFlashAttribute("mensagemSucesso", "Status do pedido atualizado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagemErro", "Erro ao atualizar status: " + e.getMessage());
        }
        return "redirect:/pedidos";
    }

    @GetMapping("/{id}/itens-json")
    @ResponseBody
    public List<Map<String, Object>> buscarItensPedido(@PathVariable Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow();
        List<Map<String, Object>> response = new ArrayList<>();

        for (ItemPedido item : pedido.getItens()) {
            Map<String, Object> dto = new HashMap<>();
            dto.put("quantidade", item.getQuantidade());
            dto.put("precoUnitario", item.getPrecoUnitario());

            if (item.getProduto() != null) {
                dto.put("id", item.getProduto().getId());
                dto.put("nome", item.getProduto().getNome());
            } else if (item.getPeca() != null) {
                dto.put("id", item.getPeca().getId());
                dto.put("nome", item.getPeca().getNome());
            } else {
                dto.put("id", item.getItemId());
                dto.put("nome", "Item Desconhecido");
            }

            response.add(dto);
        }

        return response;
    }
}