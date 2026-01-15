package Circuit.Circuit.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/funcionarios")
    public String funcionarios() {
        return "funcionarios";
    }

    @GetMapping("/clientes")
    public String clientes() {
        return "clientes";
    }

    @GetMapping("/fornecedores")
    public String fornecedores() {
        return "fornecedores";
    }

    @GetMapping("/estoque")
    public String estoque() {
        return "estoque";
    }
}