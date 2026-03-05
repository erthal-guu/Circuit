package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.ContasReceber;
import Circuit.Circuit.Model.Enum.CondicaoPagamento;
import Circuit.Circuit.Model.Enum.FormaPagamento;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import Circuit.Circuit.Repository.ContaReceberRepository;
import Circuit.Circuit.Service.ContaReceberService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/contas-receber")
public class ContarReceberController {
    @Autowired
    private ContaReceberService contaReceberService;

    @Autowired
    private ContaReceberRepository contaReceberRepository;

    @GetMapping
    public String abrirContasReceber(Model model){
       List<ContasReceber> contasReceber = contaReceberService.listarContasReceber();
       model.addAttribute("listaContasReceber",contasReceber);
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
}
