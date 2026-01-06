package Circuit.Circuit.Controller;

import Circuit.Circuit.ApiDto.viaCep;
import Circuit.Circuit.Model.Funcionario;
import Circuit.Circuit.Service.apiCepService;
import Circuit.Circuit.Service.funcionarioCadastroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class funcionarioCadastroController {
    @Autowired
    private funcionarioCadastroService funcionarioService;
    @Autowired
    private apiCepService viaCepService;
    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrar(@RequestBody Funcionario funcionarioCadastro){
        try{
            funcionarioService.cadastrar(funcionarioCadastro);
            return ResponseEntity.ok("Funcionário cadastrado com sucesso!");
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/consulta-cep/{cep}")
    public ResponseEntity<?> consultarCep(@PathVariable String cep) {
        try {
            viaCep dadosEndereco = viaCepService.consultarCep(cep);
            return ResponseEntity.ok(dadosEndereco);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/listar-Ativos")
    public List<Funcionario>ListarFuncionariosAtivos(){
        return funcionarioService.listarFuncionarioAtivos();
    }
    @GetMapping("/listar-Inativos")
    public List<Funcionario>ListarFuncionariosInativos(){
        return funcionarioService.ListarFuncionariosInativos();
    }
    @DeleteMapping("/excluir/{id}")
    public void ExcluirFuncionário(@PathVariable Long id){
        funcionarioService.ExcluirFuncionario(id);
    }
    @PutMapping("/editar/{id}")
    public Funcionario EditarFuncionario(@PathVariable Long id , @RequestBody Funcionario funcionario){
        return funcionarioService.EditarFuncionario(id,funcionario);
    }
    @PutMapping("/restaurar/{id}")
    public void RestaurarFuncionario(@PathVariable Long id){
        funcionarioService.RestaurarFuncionario(id);
    }
    @GetMapping("/pesquisar-ativos")
    public List<Funcionario> pesquisarAtivos(@RequestParam("nome") String nome){
        return funcionarioService.pesquisarFuncionarioAtivo(nome);
    }
    @GetMapping("/pesquisar-inativos")
    public List<Funcionario> pesquisarInativos(@RequestParam("nome") String nome){
        return funcionarioService.pesquisarFuncionarioInativo(nome);
    }
}
