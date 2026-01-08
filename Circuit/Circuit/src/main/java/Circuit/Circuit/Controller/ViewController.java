package Circuit.Circuit.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/usuarios")
    public String usuarios() {
        return "usuarios";
    }

    @GetMapping("/funcionarios")
    public String funcionarios() {
        return "funcionarios";
    }

    @GetMapping("/clientes")
    public String clientes() {
        return "clientes";
    }
}