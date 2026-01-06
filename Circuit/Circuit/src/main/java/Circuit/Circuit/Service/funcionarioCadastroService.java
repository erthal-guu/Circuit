package Circuit.Circuit.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Circuit.Circuit.Model.Funcionario;
import Circuit.Circuit.Repository.funcionarioCadastroRepository;

import java.util.List;

@Service
public class funcionarioCadastroService {
    @Autowired
    private funcionarioCadastroRepository funcionarioRepository;

    public Funcionario cadastrar (Funcionario funcionario) {
        if (funcionarioRepository.existsByCpf(funcionario.getCpf())) {
            throw new RuntimeException("Este CPF já está cadastrado no sistema.");
        }
        return funcionarioRepository.save(funcionario);
    }
    public List<Funcionario> listarFuncionarioAtivos(){
         return funcionarioRepository.findByAtivoTrue();
    }
    public List<Funcionario>ListarFuncionariosInativos(){
        return funcionarioRepository.findByAtivoFalse();
    }

}
