package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Cliente;
import Circuit.Circuit.Repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente cadastrar(Cliente cliente) {
        logger.info("Tentativa de cadastro de novo cliente - Nome: {}, CPF: {}, Email: {}",
                cliente.getNome(), cliente.getCpf(), cliente.getEmail());

        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            logger.warn("Falha ao cadastrar cliente: CPF já existe - {}", cliente.getCpf());
            throw new RuntimeException("Este CPF já está cadastrado no sistema.");
        }

        Cliente clienteSalvo = clienteRepository.save(cliente);
        logger.info("Cliente cadastrado com sucesso - ID: {}, Nome: {}, CPF: {}",
                clienteSalvo.getId(), clienteSalvo.getNome(), clienteSalvo.getCpf());

        return clienteSalvo;
    }

    public List<Cliente> listarAtivos() {
        List<Cliente> clientes = clienteRepository.findByAtivoTrue();
        logger.debug("Listagem de clientes ativos - {} registros encontrados", clientes.size());
        return clientes;
    }

    public List<Cliente> listarInativos() {
        List<Cliente> clientes = clienteRepository.findByAtivoFalse();
        logger.debug("Listagem de clientes inativos - {} registros encontrados", clientes.size());
        return clientes;
    }

    public Cliente ExcluirCliente(Long id) {
        Cliente cliente = clienteRepository.getReferenceById(id);
        logger.info("Tentativa de exclusão de cliente - ID: {}, Nome: {}, CPF: {}",
                cliente.getId(), cliente.getNome(), cliente.getCpf());

        cliente.setAtivo(false);
        Cliente clienteSalvo = clienteRepository.save(cliente);

        logger.info("Cliente desativado com sucesso - ID: {}, Nome: {}, CPF: {}",
                clienteSalvo.getId(), clienteSalvo.getNome(), clienteSalvo.getCpf());

        return clienteSalvo;
    }

    public Cliente RestaurarCliente(Long id) {
        Cliente cliente = clienteRepository.getReferenceById(id);
        logger.info("Tentativa de restauração de cliente - ID: {}, Nome: {}, CPF: {}",
                cliente.getId(), cliente.getNome(), cliente.getCpf());

        cliente.setAtivo(true);
        Cliente clienteSalvo = clienteRepository.save(cliente);

        logger.info("Cliente restaurado com sucesso - ID: {}, Nome: {}, CPF: {}",
                clienteSalvo.getId(), clienteSalvo.getNome(), clienteSalvo.getCpf());

        return clienteSalvo;
    }

    public Cliente editarCliente(Long id, Cliente dadosAtualizados) {
        Cliente cliente = clienteRepository.getReferenceById(id);
        logger.info("Tentativa de edição de cliente - ID: {}, Nome atual: {}, Nome novo: {}",
                id, cliente.getNome(), dadosAtualizados.getNome());

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

        Cliente clienteSalvo = clienteRepository.save(cliente);

        logger.info("Cliente editado com sucesso - ID: {}, Nome: {}, CPF: {}",
                clienteSalvo.getId(), clienteSalvo.getNome(), clienteSalvo.getCpf());

        return clienteSalvo;
    }
}
