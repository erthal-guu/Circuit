package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.*;
import Circuit.Circuit.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
