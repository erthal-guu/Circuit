package Circuit.Circuit.Controller;

import Circuit.Circuit.Dto.ServicoDto;
import Circuit.Circuit.Model.Peca;
import Circuit.Circuit.Model.Servico;
import Circuit.Circuit.Service.PecaService;
import Circuit.Circuit.Service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/cadastrar")
    public  String cadastrar(@ModelAttribute ServicoDto servicoDto, RedirectAttributes redirectAttributes) {
        try {
            servicoService.cadastrar(servicoDto);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Serviço salvo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro: " + e.getMessage());

        }
        return "redirect:/servicos";
    }
    @GetMapping("/excluir/{id}")

    public String excluir (@PathVariable Long id, RedirectAttributes redirectAttributes){
        try {
            servicoService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemDelete", "Serviço excluido com sucesso!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro: " + e.getMessage());
        }
        return "redirect:/servicos";
    }
    @GetMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try{
            servicoService.restaurar(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Serviço restaurado com sucesso!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro: " + e.getMessage());
        }
        return "redirect:/servicos";
    }
    @PostMapping("/editar")
    public String editar(@ModelAttribute ServicoDto servicoDto, Long id, RedirectAttributes redirectAttributes) {
        try {
            servicoService.editarServico(id, servicoDto);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Serviço atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar: " + e.getMessage());
        }
        return "redirect:/servicos";
    }
}
