package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.*;
import Circuit.Circuit.Repository.VendaRepository;
import Circuit.Circuit.Service.ClienteService;
import Circuit.Circuit.Service.FuncionarioService;
import Circuit.Circuit.Service.VendaService;
import jakarta.transaction.Transactional;
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
@RequestMapping("/vendas")

public class VendaController {
    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VendaService vendaService;

    @Autowired
    private VendaRepository vendaRepository;

    @GetMapping
    @Transactional
    public String abrirVendas(Model model){
        List<Cliente> clientes = clienteService.listarAtivos();
        List<Funcionario> funcionarios = funcionarioService.listarApenasVendedores();
        List<Venda> vendas = vendaService.listarTodasVendas();
        List<Venda> vendasPendentes = vendaService.listarVendasPendentes();
        List<Venda> vendasConcluidas = vendaService.listarVendasConcluidas();

        model.addAttribute("listaFuncionarios",funcionarios);
        model.addAttribute("listaClientes",clientes);
        model.addAttribute("listaTodasVendas", vendas);
        model.addAttribute("listaVendasPendentes", vendasPendentes);
        model.addAttribute("listaVendasConcluidas", vendasConcluidas);
        model.addAttribute("venda", new Venda());
        return "vendas";
    }

    @PostMapping("/cadastrar")
    public String salvar(@RequestParam(required = false) Long id,
                         @RequestParam Long clienteId,
                         @RequestParam Long funcionarioId,
                         @RequestParam BigDecimal valorBruto,
                         @RequestParam BigDecimal valorTotal,
                         @RequestParam BigDecimal porcentagemDesconto,
                         @RequestParam(required = false) String motivoDesconto,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataVenda,
                         @RequestParam String codigo,
                         @RequestParam StatusVenda status,
                         @RequestParam(required = false) FormaPagamento formaPagamento,
                         @RequestParam(required = false) CondicaoPagamento condicaoPagamento,
                         @RequestParam(required = false) Integer numeroParcelas,
                         @RequestParam List<Long> itensId,
                         @RequestParam List<Integer> quantidadeItens,
                         RedirectAttributes redirectAttributes) {
        try {
            vendaService.salvarVenda(id, clienteId, funcionarioId, valorTotal,valorBruto,porcentagemDesconto,
                    motivoDesconto, dataVenda, codigo, status, formaPagamento, condicaoPagamento, numeroParcelas, itensId, quantidadeItens);

            redirectAttributes.addFlashAttribute("mensagemSucesso", "Venda realizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro: " + e.getMessage());
        }
        return "redirect:/vendas";
    }
    @PostMapping("/atualizar-status")
    public String atulizarStatus(@RequestParam Long vendaId,@RequestParam StatusVenda novoStatus,RedirectAttributes ra){
        try{

        vendaService.atualizarStatus(vendaId,novoStatus);
        ra.addFlashAttribute("mensagemSucesso", "Status da venda atualizado com sucesso!");
    } catch (Exception e){
            ra.addFlashAttribute("mensagemErro", "Erro ao atualizar status: " + e.getMessage());
        }
        return "redirect:/vendas";
    }
    @GetMapping("/{id}/itens-json")
    @ResponseBody
    public List<Map<String, Object>> buscarItensVenda(@PathVariable Long id) {
        Venda venda = vendaRepository.findById(id).orElseThrow();
        List<Map<String, Object>> response = new ArrayList<>();

        for (ItemVenda item : venda.getItens()) {
            Map<String, Object> dto = new HashMap<>();
            dto.put("quantidade", item.getQuantidade());
            dto.put("precoUnitario", item.getPrecoUnitario());

            if (item.getProduto() != null) {
                dto.put("id", item.getProduto().getId());
                dto.put("nome", item.getProduto().getNome());
            } else {
                dto.put("id", item.getId());
                dto.put("nome", "Item Desconhecido");
            }
            response.add(dto);
        }
        return response;
    }
}
