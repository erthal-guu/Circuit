package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.Cliente;
import Circuit.Circuit.Model.FormaPagamento;
import Circuit.Circuit.Model.Funcionario;
import Circuit.Circuit.Model.Venda;
import Circuit.Circuit.Service.ClienteService;
import Circuit.Circuit.Service.FuncionarioService;
import Circuit.Circuit.Service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/vendas")

public class VendaController {
    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VendaService vendaService;

    @GetMapping
    public String abrirVendas(Model model){
        List<Cliente> clientes = clienteService.listarAtivos();
        List<Funcionario> funcionarios = funcionarioService.listarApenasVendedores();

        model.addAttribute("listaFuncionarios",funcionarios);
        model.addAttribute("listaClietes",clientes);
        return "vendas";
    }
}
