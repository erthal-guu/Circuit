package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.Fornecedor;
import Circuit.Circuit.Model.Funcionario;
import Circuit.Circuit.Service.FornecedorService;
import Circuit.Circuit.Service.FuncionarioService;
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
    FornecedorService fornecedorService;

    @Autowired
    FuncionarioService funcionarioService;

    @GetMapping
    public String abrirPedido(Model model){
        List<Fornecedor> fornecedores = fornecedorService.listarFornecedoresAtivos();
        List<Funcionario> funcionarios = funcionarioService.listarFuncionarioAtivos();
        model.addAttribute("listaFornecedores",fornecedores);
        model.addAttribute("listaFuncionarios",funcionarios);
        return "pedidos";
    }
}
