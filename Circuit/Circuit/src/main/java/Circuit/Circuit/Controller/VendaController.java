package Circuit.Circuit.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vendas")
public class VendaController {
    @GetMapping
    public String abrirVendas(){
        return "vendas";
    }
}
