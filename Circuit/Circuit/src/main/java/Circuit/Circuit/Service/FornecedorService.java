package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Fornecedor;
import Circuit.Circuit.Repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    public Fornecedor cadastrar(Fornecedor fornecedor) {
        if (fornecedorRepository.existsByCnpj(fornecedor.getCnpj())) {
            throw new RuntimeException("Este CNPJ já está cadastrado no sistema.");
        }
        fornecedor.setAtivo(true);
        return fornecedorRepository.save(fornecedor);
    }

    public List<Fornecedor> listarFornecedoresAtivos() {
        return fornecedorRepository.findByAtivoTrueOrderById();
    }

    public List<Fornecedor> listarFornecedoresInativos() {
        return fornecedorRepository.findByAtivoFalseOrderById();
    }

    public Fornecedor excluirFornecedor(Long id) {
        Fornecedor fornecedor = fornecedorRepository.getReferenceById(id);
        fornecedor.setAtivo(false);
        return fornecedorRepository.save(fornecedor);
    }

    public Fornecedor restaurarFornecedor(Long id) {
        Fornecedor fornecedor = fornecedorRepository.getReferenceById(id);
        fornecedor.setAtivo(true);
        return fornecedorRepository.save(fornecedor);
    }

    public Fornecedor editarFornecedor(Long id, Fornecedor dadosAtualizados) {
        Fornecedor fornecedor = fornecedorRepository.getReferenceById(id);
        fornecedor.setNomeFantasia(dadosAtualizados.getNomeFantasia());
        fornecedor.setRazaoSocial(dadosAtualizados.getRazaoSocial());
        fornecedor.setCnpj(dadosAtualizados.getCnpj());
        fornecedor.setTelefone(dadosAtualizados.getTelefone());
        fornecedor.setEmail(dadosAtualizados.getEmail());
        fornecedor.setCep(dadosAtualizados.getCep());
        fornecedor.setLogradouro(dadosAtualizados.getLogradouro());
        fornecedor.setNumero(dadosAtualizados.getNumero());
        fornecedor.setBairro(dadosAtualizados.getBairro());
        fornecedor.setCidade(dadosAtualizados.getCidade());
        fornecedor.setEstado(dadosAtualizados.getEstado());
        fornecedor.setAtivo(dadosAtualizados.getAtivo());
        return fornecedorRepository.save(fornecedor);
    }

    public List<Fornecedor> pesquisarFornecedorAtivo(String nome) {
        if (nome == null) return fornecedorRepository.findByAtivoTrueOrderById();
        return fornecedorRepository.findByNomeFantasiaContainingIgnoreCaseAndAtivoTrue(nome);
    }

    public List<Fornecedor> pesquisarFornecedorInativo(String nome) {
        if (nome == null) return fornecedorRepository.findByAtivoFalseOrderById();
        return fornecedorRepository.findByNomeFantasiaContainingIgnoreCaseAndAtivoFalse(nome);
    }
}