package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.ContasPagar;
import Circuit.Circuit.Model.Enum.CondicaoPagamento;
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
        model.addAttribute("totalPago", contasPagarService.calcularTotalPago());
        model.addAttribute("totalCancelado", contasPagarService.calcularTotalCancelado());
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

    @GetMapping("/excluir/{id}")
    public String cancelarConta(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try {
            contasPagarService.cancelarConta(id);
            redirectAttributes.addFlashAttribute("mensagemDelete", "Conta cancelada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/contas-pagar";
    }

    @PostMapping("/editar")
    public String editarConta(@RequestParam Long id,
                               @RequestParam Long fornecedor,
                               @RequestParam BigDecimal valor,
                               @RequestParam(required = false) BigDecimal valorPago,
                               @RequestParam LocalDate dataVencimento,
                               @RequestParam(required = false) LocalDate dataPagamento,
                               @RequestParam(required = false) FormaPagamento formaPagamento,
                               @RequestParam(required = false) CondicaoPagamento condicaoPagamento,
                               @RequestParam(required = false) Integer numeroParcelas,
                               RedirectAttributes redirectAttributes) {
        try {
            contasPagarService.editarConta(id, fornecedor, valor, valorPago, dataVencimento, dataPagamento, formaPagamento, condicaoPagamento, numeroParcelas);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Conta atualizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar: " + e.getMessage());
        }
        return "redirect:/contas-pagar";
    }
}
