package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.OrdemServico;
import Circuit.Circuit.Service.OrdemServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("ordens-servico")
public class OrdemServicoController {
    @Autowired
    private OrdemServicoService ordemServicoService;



    @GetMapping
    public String abrirOrdem(Model model) {
     return "ordens-servico";
    }
}
