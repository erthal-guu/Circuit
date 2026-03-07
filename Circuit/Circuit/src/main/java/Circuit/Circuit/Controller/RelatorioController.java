package Circuit.Circuit.Controller;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {
   @GetMapping
   public String abrirRelatorios(Model model){
    return "relatorios";
   }

}
