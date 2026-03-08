package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Enum.Cargo;
import Circuit.Circuit.Model.Funcionario;
import Circuit.Circuit.Repository.FuncionarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    private static final Logger logger = LoggerFactory.getLogger(FuncionarioService.class);

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public Funcionario cadastrar(Funcionario funcionario) {
        logger.info("Tentativa de cadastro de novo funcionário - Nome: {}, CPF: {}, Cargo: {}",
                funcionario.getNome(), funcionario.getCpf(), funcionario.getCargo());

        if (funcionarioRepository.existsByCpf(funcionario.getCpf())) {
            logger.warn("Falha ao cadastrar funcionário: CPF já existe - {}", funcionario.getCpf());
            throw new RuntimeException("Este CPF já está cadastrado no sistema.");
        }

        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);
        logger.info("Funcionário cadastrado com sucesso - ID: {}, Nome: {}, CPF: {}, Cargo: {}",
                funcionarioSalvo.getId(), funcionarioSalvo.getNome(), funcionarioSalvo.getCpf(), funcionarioSalvo.getCargo());

        return funcionarioSalvo;
    }

    public List<Funcionario> listarFuncionarioAtivos() {
        List<Funcionario> funcionarios = funcionarioRepository.findByAtivoTrueOrderById();
        logger.debug("Listagem de funcionários ativos - {} registros encontrados", funcionarios.size());
        return funcionarios;
    }

    public List<Funcionario> ListarFuncionariosInativos() {
        List<Funcionario> funcionarios = funcionarioRepository.findByAtivoFalseOrderById();
        logger.debug("Listagem de funcionários inativos - {} registros encontrados", funcionarios.size());
        return funcionarios;
    }
    public List<Funcionario> listarApenasVendedores(){
        List<Funcionario> vendedores = funcionarioRepository.findByAtivoAndCargoOrderById(true, Cargo.VENDEDOR);
        logger.debug("Listagem de vendedores - {} registros encontrados", vendedores.size());
        return vendedores;
    }
    public List<Funcionario> listarApenasTecnicos() {
        List<Funcionario> tecnicos = funcionarioRepository.findByAtivoAndCargoOrderById(true, Cargo.TECNICO);
        logger.debug("Listagem de técnicos - {} registros encontrados", tecnicos.size());
        return tecnicos;
    }

    public Funcionario ExcluirFuncionario(Long id) {
        Funcionario funcionario = funcionarioRepository.getReferenceById(id);
        logger.info("Tentativa de exclusão de funcionário - ID: {}, Nome: {}, CPF: {}, Cargo: {}",
                funcionario.getId(), funcionario.getNome(), funcionario.getCpf(), funcionario.getCargo());

        funcionario.setAtivo(false);
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        logger.info("Funcionário desativado com sucesso - ID: {}, Nome: {}, CPF: {}, Cargo: {}",
                funcionarioSalvo.getId(), funcionarioSalvo.getNome(), funcionarioSalvo.getCpf(), funcionarioSalvo.getCargo());

        return funcionarioSalvo;
    }

    public Funcionario EditarFuncionario(Long id, Funcionario dadosAtualizados) {
        Funcionario funcionario = funcionarioRepository.getReferenceById(id);
        logger.info("Tentativa de edição de funcionário - ID: {}, Nome atual: {}, Nome novo: {}, Cargo novo: {}",
                id, funcionario.getNome(), dadosAtualizados.getNome(), dadosAtualizados.getCargo());

        funcionario.setNome(dadosAtualizados.getNome());
        funcionario.setCpf(dadosAtualizados.getCpf());
        funcionario.setCargo(dadosAtualizados.getCargo());
        funcionario.setTelefone(dadosAtualizados.getTelefone());
        funcionario.setEmail(dadosAtualizados.getEmail());
        funcionario.setDataAdmissao(dadosAtualizados.getDataAdmissao());
        funcionario.setAtivo(dadosAtualizados.getAtivo());
        funcionario.setCep(dadosAtualizados.getCep());
        funcionario.setLogradouro(dadosAtualizados.getLogradouro());
        funcionario.setNumero(dadosAtualizados.getNumero());
        funcionario.setBairro(dadosAtualizados.getBairro());
        funcionario.setCidade(dadosAtualizados.getCidade());
        funcionario.setEstado(dadosAtualizados.getEstado());

        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        logger.info("Funcionário editado com sucesso - ID: {}, Nome: {}, CPF: {}, Cargo: {}",
                funcionarioSalvo.getId(), funcionarioSalvo.getNome(), funcionarioSalvo.getCpf(), funcionarioSalvo.getCargo());

        return funcionarioSalvo;
    }

    public Funcionario RestaurarFuncionario(Long id) {
        Funcionario funcionario = funcionarioRepository.getReferenceById(id);
        logger.info("Tentativa de restauração de funcionário - ID: {}, Nome: {}, CPF: {}, Cargo: {}",
                funcionario.getId(), funcionario.getNome(), funcionario.getCpf(), funcionario.getCargo());

        funcionario.setAtivo(true);
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        logger.info("Funcionário restaurado com sucesso - ID: {}, Nome: {}, CPF: {}, Cargo: {}",
                funcionarioSalvo.getId(), funcionarioSalvo.getNome(), funcionarioSalvo.getCpf(), funcionarioSalvo.getCargo());

        return funcionarioSalvo;
    }



}
