package Circuit.Circuit.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contas-pagar")

public class ContasPagarController {

    @GetMapping
    public String abrirContas(){
        return "contas-pagar";
    }
}
