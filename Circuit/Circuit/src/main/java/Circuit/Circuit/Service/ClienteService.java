package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Cliente;
import Circuit.Circuit.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente cadastrar(Cliente cliente) {
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new RuntimeException("Este CPF já está cadastrado no sistema.");
        }
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarAtivos() {
        return clienteRepository.findByAtivoTrue();
    }

    public List<Cliente> listarInativos() {
        return clienteRepository.findByAtivoFalse();
    }
}
