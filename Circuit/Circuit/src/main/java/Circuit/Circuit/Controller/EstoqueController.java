package Circuit.Circuit.Controller;

import Circuit.Circuit.Service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/estoque/movimentacao")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;
    @PostMapping("/entrada")
    public String entrada(@RequestParam(value = "ids", required = false) List<Long> ids, @RequestParam("quantidade") Integer quantidade,
                          RedirectAttributes redirectAttributes) {

        if (ids == null || ids.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Nenhum produto selecionado!");
            return "redirect:/estoque";
        }

        try {
            for (Long id : ids) {
                estoqueService.alimentarEstoque(id, quantidade);
            }
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Entrada realizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro: " + e.getMessage());
        }

        return "redirect:/estoque";
    }

    @PostMapping("/saida")
    public String saida(@RequestParam(value = "ids", required = false) List<Long> ids,
                              @RequestParam("quantidade") Integer quantidade,
                              RedirectAttributes redirectAttributes) {

        if (ids == null || ids.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Nenhum produto selecionado!");
            return "redirect:/estoque";
        }

        try {
            for (Long id : ids) {
                estoqueService.retirarEstoque(id, quantidade);
            }
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Saída realizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro: " + e.getMessage());
        }

        return "redirect:/estoque";
    }
}