package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.*;
import Circuit.Circuit.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/ordens-servico")
public class OrdemServicoController {
    @Autowired
    private OrdemServicoService ordemServicoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private AparelhoService aparelhoService;

    @Autowired
    private PecaService pecaService;

    @GetMapping
    public String abrirOrdem(Model model) {
        List<Funcionario> tecnicos = funcionarioService.listarApenasTecnicos();
        List<Cliente> clientes = clienteService.listarAtivos();
        List<Servico> servicos = servicoService.ListarAtivos();
        List<Peca> pecas = pecaService.listarPecasAtivas();
        List<OrdemServico> ordens = ordemServicoService.ListarOrdens();
        List<OrdemServico> ordensAbertas = ordemServicoService.ListarOrdensAbertas();
        List<OrdemServico> ordensFinalizadas = ordemServicoService.ListarOrdensFinalizadas();
        model.addAttribute("listaTecnicos",tecnicos);
        model.addAttribute("listaClientes",clientes);
        model.addAttribute("listaServicos",servicos);
        model.addAttribute("listaOrdens",ordens);
        model.addAttribute("listaOrdensAbertas",ordensAbertas);
        model.addAttribute("listaOrdensFinalizadas",ordensFinalizadas);
        model.addAttribute("listaPecas",pecas);
        model.addAttribute("ordem", new OrdemServico());
     return "ordens-servico";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute OrdemServico ordemServico, RedirectAttributes redirectAttributes){
        try{
            ordemServicoService.cadastrar(ordemServico);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Ordem gerada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/ordens-servico";
    }

    @PostMapping("/editar")
    public String editar(@ModelAttribute OrdemServico ordemServico, RedirectAttributes redirectAttributes) {
        try {
            ordemServicoService.editar(ordemServico.getId(), ordemServico);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Ordem atualizada!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro: " + e.getMessage());
        }
        return "redirect:/ordens-servico";
    }
    @PostMapping("/atualizar-status")
    public String atualizarStatus(@RequestParam Long id, @RequestParam Status status, RedirectAttributes redirectAttributes) {
        try {
            ordemServicoService.atualizarStatus(id, status);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Status atualizado com sucesso");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Erro ao atualizar status: " + e.getMessage());
        }
        return "redirect:/ordens-servico";
    }
    }

