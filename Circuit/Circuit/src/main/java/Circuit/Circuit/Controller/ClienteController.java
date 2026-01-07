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
}
