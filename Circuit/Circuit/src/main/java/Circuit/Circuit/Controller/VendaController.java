package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.*;
import Circuit.Circuit.Service.ClienteService;
import Circuit.Circuit.Service.FuncionarioService;
import Circuit.Circuit.Service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
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
        model.addAttribute("venda", new Venda());
        return "vendas";
    }

    @PostMapping("/cadastrar")
    public String salvar(@RequestParam(required = false) Long id,
                         @RequestParam Long clienteId,
                         @RequestParam Long funcionarioId,
                         @RequestParam BigDecimal valorTotal,
                         @RequestParam(required = false) String motivoDesconto,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataVenda,
                         @RequestParam List<Long> itensId,
                         @RequestParam List<Integer> quantidadeItens,
                         RedirectAttributes redirectAttributes) {
        try {
            vendaService.salvarVenda(id, clienteId, funcionarioId, valorTotal,
                    motivoDesconto, dataVenda, itensId, quantidadeItens);

            redirectAttributes.addFlashAttribute("mensagemSucesso", "Venda realizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro: " + e.getMessage());
        }
        return "redirect:/vendas";
    }
}
