package Circuit.Circuit.Controller;

import Circuit.Circuit.ApiDto.viaCep;
import Circuit.Circuit.Model.Cliente;
import Circuit.Circuit.Service.CepService;
import Circuit.Circuit.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private CepService viaCepService;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrar(@RequestBody Cliente cliente) {
        try {
            clienteService.cadastrar(cliente);
            return ResponseEntity.ok("Cliente cadastrado com sucesso!");
        } catch (RuntimeException e) {
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

    @GetMapping("/listar-ativos")
    public List<Cliente> listarClientesAtivos() {
        return clienteService.listarAtivos();
    }

    @GetMapping("/listar-inativos")
    public List<Cliente> listarClientesInativos() {
        return clienteService.listarInativos();
    }

    @GetMapping("/pesquisar-ativos")
    public List<Cliente> pesquisarClientesAtivos(@RequestParam("nome") String nome) {
        return clienteService.pesquisarClienteAtivos(nome);
    }

    @GetMapping("/pesquisar-inativos")
    public List<Cliente> pesquisarClientesInativos(@RequestParam("nome") String nome) {
        return clienteService.pesquisarClienteInativo(nome);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluirCliente(@PathVariable Long id) {
        try {
            clienteService.ExcluirCliente(id);
            return ResponseEntity.ok("Cliente desativado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/restaurar/{id}")
    public ResponseEntity<?> restaurarClientes(@PathVariable Long id) {
        try {
            clienteService.RestaurarCliente(id);
            return ResponseEntity.ok("Cliente restaurado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarClientes(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            Cliente atualizado = clienteService.editarCliente(id, cliente);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
