package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.*;
import Circuit.Circuit.Repository.OrdemServicoRepository;
import Circuit.Circuit.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping
    public String abrirOrdem(Model model) {
        List<Funcionario> tecnicos = funcionarioService.listarApenasTecnicos();
        List<Cliente> clientes = clienteService.listarAtivos();
        List<Servico> servicos = servicoService.ListarAtivos();
        List<OrdemServico> ordens = ordemServicoService.ListarOrdens();
        model.addAttribute("listaTecnicos",tecnicos);
        model.addAttribute("listaClientes",clientes);
        model.addAttribute("listaServicos",servicos);
        model.addAttribute("listaOrdens",ordens);
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
}
