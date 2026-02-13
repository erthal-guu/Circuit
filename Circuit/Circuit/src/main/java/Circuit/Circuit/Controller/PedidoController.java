package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.*;
import Circuit.Circuit.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
        public String salvar(@RequestParam Long fornecedorId,
                             @RequestParam Long responsavelId,
                             @RequestParam String numeroPedido,
                             @RequestParam BigDecimal valorTotal,
                             @RequestParam(required = false) String observacao,
                             @RequestParam String tipoPedido,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataPedido,
                             @RequestParam(required = false) List<Long> itensId,
                             @RequestParam(required = false) List<Integer> quantidadeItens,
                             @RequestParam(required = false) List<BigDecimal> precoItens,
                             RedirectAttributes redirectAttributes) {
            if (itensId == null || itensId.isEmpty()) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Adicione pelo menos um item ao pedido!");
                return "redirect:/pedidos";
            }
            try {
                pedidoService.salvarPedido(fornecedorId, responsavelId, numeroPedido,
                        observacao, tipoPedido, dataPedido,valorTotal,
                        itensId, quantidadeItens, precoItens);

                redirectAttributes.addFlashAttribute("mensagemSucesso", "Pedido salvo com sucesso!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar pedido: " + e.getMessage());
            }

            return "redirect:/pedidos";
        }
    @PostMapping("/atualizar-status")
    public String atualizarStatus(@RequestParam Long pedidoId,
                                  @RequestParam StatusPedido novoStatus,
                                  RedirectAttributes ra) {
        try {
            pedidoService.atualizarStatus(pedidoId, novoStatus);
            ra.addFlashAttribute("mensagemSucesso", "Status do pedido atualizado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("mensagemErro", "Erro ao atualizar status: " + e.getMessage());
        }
        return "redirect:/pedidos";
    }
    }
