package Circuit.Circuit.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Circuit.Circuit.Model.Funcionario;
import Circuit.Circuit.Repository.FuncionarioRepository;

import java.util.List;

@Service
public class FuncionarioService {
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public Funcionario cadastrar(Funcionario funcionario) {
        if (funcionarioRepository.existsByCpf(funcionario.getCpf())) {
            throw new RuntimeException("Este CPF já está cadastrado no sistema.");
        }
        return funcionarioRepository.save(funcionario);
    }

    public List<Funcionario> listarFuncionarioAtivos() {
        return funcionarioRepository.findByAtivoTrueOrderById();
    }

    public List<Funcionario> ListarFuncionariosInativos() {
        return funcionarioRepository.findByAtivoFalseOrderById();
    }

    public Funcionario ExcluirFuncionario(Long id) {
        Funcionario funcionario = funcionarioRepository.getReferenceById(id);
        funcionario.setAtivo(false);
        return funcionarioRepository.save(funcionario);
    }

    public Funcionario EditarFuncionario(Long id, Funcionario dadosAtualizados) {
        Funcionario funcionario = funcionarioRepository.getReferenceById(id);
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
        return funcionarioRepository.save(funcionario);
    }

    public Funcionario RestaurarFuncionario(Long id) {
        Funcionario funcionario = funcionarioRepository.getReferenceById(id);
        funcionario.setAtivo(true);
        return funcionarioRepository.save(funcionario);
    }

}
