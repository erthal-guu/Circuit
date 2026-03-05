package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.Cliente;
import Circuit.Circuit.Model.ContasReceber;
import Circuit.Circuit.Model.Enum.CondicaoPagamento;
import Circuit.Circuit.Model.Enum.FormaPagamento;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import Circuit.Circuit.Repository.ContaReceberRepository;
import Circuit.Circuit.Service.ClienteService;
import Circuit.Circuit.Service.ContaReceberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/contas-receber")
public class ContarReceberController {
    @Autowired
    private ContaReceberService contaReceberService;

    @Autowired
    private ContaReceberRepository contaReceberRepository;
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String abrirContasReceber(Model model){
       List<ContasReceber> contasReceber = contaReceberService.listarContasReceber();
       List<Cliente> clientes  = clienteService.listarAtivos();
       model.addAttribute("listaContasReceber",contasReceber);
       model.addAttribute("listaClientes",clientes);
       model.addAttribute("contas-receber", new ContasReceber());

         return "contas-receber";
     }

    @PostMapping("/receber")
    public String receberPagamento(@RequestParam Long id,
                                    @RequestParam BigDecimal valorRecebido,
                                    @RequestParam LocalDate dataPagamento,

                                    @RequestParam(required = false) FormaPagamento formaPagamento,
                                    @RequestParam(required = false) CondicaoPagamento condicaoPagamento,
                                    @RequestParam(required = false) Integer numeroParcelas,
                                    RedirectAttributes redirectAttributes) {
 
        try {
            contaReceberService.receberPagamento(id, valorRecebido, dataPagamento, formaPagamento, condicaoPagamento, numeroParcelas);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Pagamento recebido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/contas-receber";
    }

    @PostMapping("/atualizar-status")
    public String atualizarStatus(@RequestParam Long id,
                                   @RequestParam StatusFinanceiro status,
                                   RedirectAttributes redirectAttributes) {
        try {
            contaReceberService.atualizarStatus(id, status);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Status atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/contas-receber";
    }
    @GetMapping("/excluir/{id}")
    public String calcelarConta(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try {
            contaReceberService.cancelarConta(id);
            redirectAttributes.addFlashAttribute("mensagemDelete", "Conta cancelada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/contas-receber";
    }

    @PostMapping("/editar")
    public String editarConta(@RequestParam Long id,
                               @RequestParam Long cliente,
                               @RequestParam BigDecimal valor,
                               @RequestParam(required = false) BigDecimal valorRecebido,
                               @RequestParam LocalDate dataVencimento,
                               @RequestParam(required = false) LocalDate dataPagamento,
                               @RequestParam(required = false) FormaPagamento formaPagamento,
                               @RequestParam(required = false) CondicaoPagamento condicaoPagamento,
                               @RequestParam(required = false) Integer numeroParcelas,
                               RedirectAttributes redirectAttributes) {
        try {
            contaReceberService.editarConta(id, cliente, valor, valorRecebido, dataVencimento, dataPagamento, formaPagamento, condicaoPagamento, numeroParcelas);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Conta atualizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar: " + e.getMessage());
        }
        return "redirect:/contas-receber";
    }
}
