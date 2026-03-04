package Circuit.Circuit.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contas-receber")
public class ContarReceberController {
    @GetMapping
    public String abrirContasReceber(){
        return "contas-receber";
    }
}
