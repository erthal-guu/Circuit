package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.ContasPagar;
import Circuit.Circuit.Model.Enum.FormaPagamento;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import Circuit.Circuit.Repository.ContaPagarRepository;
import Circuit.Circuit.Service.ContasPagarService;
import Circuit.Circuit.Service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/contas-pagar")
public class ContasPagarController {

    @Autowired
    private ContasPagarService contasPagarService;

    @Autowired
    private ContaPagarRepository contaPagarRepository;

    @Autowired
    private FornecedorService fornecedorService;

    @GetMapping
    public String abrirContas(Model model){
        List<ContasPagar> contasPagar = contasPagarService.listarContasPagar();
        model.addAttribute("listaContasPagar", contasPagar);
        model.addAttribute("contas-pagar", new ContasPagar());
        model.addAttribute("listaFornecedores", fornecedorService.listarFornecedoresAtivos());
        model.addAttribute("totalPendente", contasPagarService.calcularTotalPendente());
        model.addAttribute("totalVencido", contasPagarService.calcularTotalVencido());
        model.addAttribute("totalPago", contasPagarService.calcularTotalPago());
        return "contas-pagar";
    }

    @PostMapping("/pagar")
    public String pagarConta(@RequestParam Long id,
                             @RequestParam BigDecimal valorPago,
                             @RequestParam LocalDate dataPagamento,
                             @RequestParam FormaPagamento formaPagamento,
                             RedirectAttributes redirectAttributes) {

        try {
            contasPagarService.pagarConta(id, valorPago, dataPagamento, formaPagamento);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Pagamento realizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/contas-pagar";
    }

    @PostMapping("/atualizar-status")
    public String atualizarStatus(@RequestParam Long id,
                                   @RequestParam StatusFinanceiro status,
                                   RedirectAttributes redirectAttributes) {
        try {
            contasPagarService.atualizarStatus(id, status);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Status atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/contas-pagar";
    }
}
