package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.Marca;
import Circuit.Circuit.Model.Modelo;
import Circuit.Circuit.Repository.MarcaRepository;
import Circuit.Circuit.Service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/modelos")
public class ModeloController {
    @Autowired
    private ModeloService modeloService;

    @Autowired
    private MarcaRepository marcaRepository;


    @GetMapping
    public String abrirModelos(Model model){
        List<Modelo> ativos = modeloService.ListarModelosAtivos();
        List<Modelo> inativos = modeloService.ListarModelosInativos();
        List<Marca>  marcas = marcaRepository.findAllByOrderByNomeAsc();
        model.addAttribute("listaAtivos", ativos);
        model.addAttribute("listaInativos", inativos);
        model.addAttribute("ListaMarcas", marcas);
        return "modelos";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute Modelo modelo, RedirectAttributes redirectAttributes){
        try{
            modeloService.cadastrar(modelo);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Modelo salvo com sucesso!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/modelos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id , RedirectAttributes redirectAttributes){
        try{
            modeloService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Modelo excluído com sucesso!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/modelos";
    }

    @GetMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try{
            modeloService.restaurar(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Modelo restaurado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/modelos";
    }

    @PostMapping("/editar")
    public String editar(@ModelAttribute Modelo modelo, RedirectAttributes redirectAttributes){
        try{
        modeloService.editar(modelo.getId(),modelo);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Modelo atualizado com sucesso!");
    }catch (Exception e){
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/modelos";
    }
}
