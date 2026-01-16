package Circuit.Circuit.Controller;

import Circuit.Circuit.ApiDto.viaCep;
import Circuit.Circuit.Model.Funcionario;
import Circuit.Circuit.Service.CepService;
import Circuit.Circuit.Service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private CepService viaCepService;

    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute Funcionario funcionario, RedirectAttributes redirectAttributes) {
        try {
            funcionarioService.cadastrar(funcionario);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Funcionário salvo com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro: " + e.getMessage());
        }
        return "redirect:/funcionarios";
    }

    @GetMapping("/consulta-cep-funcionarios/{cep}")
    public ResponseEntity<?> consultarCep(@PathVariable String cep) {
        try {
            viaCep dadosEndereco = viaCepService.consultarCep(cep);
            return ResponseEntity.ok(dadosEndereco);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping
    public String listarFuncionarios(Model model) {
        List<Funcionario> ativos =funcionarioService.listarFuncionarioAtivos();
        List<Funcionario> Inativos =funcionarioService.ListarFuncionariosInativos();
        model.addAttribute("listaAtivos",ativos);
        model.addAttribute("listaInativos",Inativos);
        model.addAttribute("funcionarios", new Funcionario());

        return "funcionarios";
    }
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            funcionarioService.ExcluirFuncionario(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Funcionário desativado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/funcionarios";
    }

    @PostMapping("/editar")
    public String EditarFuncionario(@ModelAttribute Funcionario funcionario, RedirectAttributes redirectAttributes) {
        try{
         funcionarioService.EditarFuncionario(funcionario.getId(), funcionario);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Funcionário atualizado com sucesso!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao editar: " + e.getMessage());
    }
        return "redirect:/funcionarios";
}

    @GetMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            funcionarioService.RestaurarFuncionario(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Funcionário restaurado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao restaurar: " + e.getMessage());
        }
        return "redirect:/funcionarios";
    }
}
