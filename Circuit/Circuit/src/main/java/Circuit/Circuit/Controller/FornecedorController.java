package Circuit.Circuit.Controller;

import Circuit.Circuit.ApiDto.viaCep;
import Circuit.Circuit.Model.Fornecedor;
import Circuit.Circuit.Service.CepService;
import Circuit.Circuit.Service.FornecedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private CepService viaCepService;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrar(@RequestBody Fornecedor fornecedor) {
        try {
            fornecedorService.cadastrar(fornecedor);
            return ResponseEntity.ok("Fornecedor cadastrado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/consulta-cep-fornecedor/{cep}")
    public ResponseEntity<?> consultarCep(@PathVariable String cep) {
        try {
            viaCep dadosEndereco = viaCepService.consultarCep(cep);
            return ResponseEntity.ok(dadosEndereco);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar-ativos")
    public List<Fornecedor> listarFornecedoresAtivos() {
        return fornecedorService.listarFornecedoresAtivos();
    }

    @GetMapping("/listar-inativos")
    public List<Fornecedor> listarFornecedoresInativos() {
        return fornecedorService.listarFornecedoresInativos();
    }

    @GetMapping("/pesquisar-ativos")
    public List<Fornecedor> pesquisarFornecedoresAtivos(@RequestParam("nome") String nome) {
        return fornecedorService.pesquisarFornecedorAtivo(nome);
    }

    @GetMapping("/pesquisar-inativos")
    public List<Fornecedor> pesquisarFornecedoresInativos(@RequestParam("nome") String nome) {
        return fornecedorService.pesquisarFornecedorInativo(nome);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluirFornecedor(@PathVariable Long id) {
        try {
            fornecedorService.excluirFornecedor(id);
            return ResponseEntity.ok("Fornecedor desativado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/restaurar/{id}")
    public ResponseEntity<?> restaurarFornecedor(@PathVariable Long id) {
        try {
            fornecedorService.restaurarFornecedor(id);
            return ResponseEntity.ok("Fornecedor restaurado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarFornecedor(@PathVariable Long id, @RequestBody Fornecedor fornecedor) {
        try {
            Fornecedor atualizado = fornecedorService.editarFornecedor(id, fornecedor);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}