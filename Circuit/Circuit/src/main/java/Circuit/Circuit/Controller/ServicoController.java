package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.Peca;
import Circuit.Circuit.Model.Servico;
import Circuit.Circuit.Service.PecaService;
import Circuit.Circuit.Service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/servicos")
public class ServicoController {
    @Autowired
    private ServicoService servicoService;

    @Autowired
    private PecaService pecaService;

    @GetMapping
    public String ListarServicos(Model model){
        List<Servico> ativos = servicoService.ListarAtivos();
        List<Servico> inativos = servicoService.ListarInativos();
        List<Peca> pecas = pecaService.listarPecasAtivas();

        model.addAttribute("listaAtivos", ativos);
        model.addAttribute("listaInativos", inativos);
        model.addAttribute("listaPecas", pecas);

        return "servicos";
    }
}
