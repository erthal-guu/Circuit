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

    public List<Cliente> pesquisarClienteAtivos(String nome) {
        if (nome == null) return clienteRepository.findByAtivoTrue();
        return clienteRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
    }

    public List<Cliente> pesquisarClienteInativo(String nome) {
        if (nome == null) return clienteRepository.findByAtivoFalse();
        return clienteRepository.findByNomeContainingIgnoreCaseAndAtivoFalse(nome);
    }

    public Cliente ExcluirCliente(Long id) {
        Cliente cliente = clienteRepository.getReferenceById(id);
        cliente.setAtivo(false);
        return clienteRepository.save(cliente);
    }

    public Cliente RestaurarCliente(Long id) {
        Cliente cliente = clienteRepository.getReferenceById(id);
        cliente.setAtivo(true);
        return clienteRepository.save(cliente);
    }

    public Cliente editarCliente(Long id, Cliente dadosAtualizados) {
        Cliente cliente = clienteRepository.getReferenceById(id);

        cliente.setNome(dadosAtualizados.getNome());
        cliente.setCpf(dadosAtualizados.getCpf());
        cliente.setEmail(dadosAtualizados.getEmail());
        cliente.setTelefone(dadosAtualizados.getTelefone());
        cliente.setCep(dadosAtualizados.getCep());
        cliente.setLogradouro(dadosAtualizados.getLogradouro());
        cliente.setNumero(dadosAtualizados.getNumero());
        cliente.setBairro(dadosAtualizados.getBairro());
        cliente.setCidade(dadosAtualizados.getCidade());
        cliente.setEstado(dadosAtualizados.getEstado());
        cliente.setAtivo(dadosAtualizados.getAtivo());

        return clienteRepository.save(cliente);
    }
}
