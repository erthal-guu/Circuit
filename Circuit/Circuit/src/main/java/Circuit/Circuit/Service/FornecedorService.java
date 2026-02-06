package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Fornecedor;
import Circuit.Circuit.Model.Peca;
import Circuit.Circuit.Model.Produto;
import Circuit.Circuit.Repository.FornecedorRepository;
import Circuit.Circuit.Repository.PecaRepository;
import Circuit.Circuit.Repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PecaRepository pecaRepository;

    public Fornecedor cadastrar(Fornecedor fornecedor) {
        if (fornecedorRepository.existsByCnpj(fornecedor.getCnpj())) {
            throw new RuntimeException("Este CNPJ já está cadastrado no sistema.");
        }
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
        fornecedor.setTipo(dadosAtualizados.getTipo());
        fornecedor.setCep(dadosAtualizados.getCep());
        fornecedor.setLogradouro(dadosAtualizados.getLogradouro());
        fornecedor.setNumero(dadosAtualizados.getNumero());
        fornecedor.setBairro(dadosAtualizados.getBairro());
        fornecedor.setCidade(dadosAtualizados.getCidade());
        fornecedor.setEstado(dadosAtualizados.getEstado());
        fornecedor.setAtivo(dadosAtualizados.getAtivo());
        return fornecedorRepository.save(fornecedor);
    }
    @Transactional
    public void vincularItens(Long fornecedorId, List<Long> itensIds, String tipo) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        if ("pecas".equalsIgnoreCase(tipo)) {
            List<Peca> pecasSelecionadas = pecaRepository.findAllById(itensIds);

            fornecedor.getPecas().clear();
            fornecedor.getPecas().addAll(pecasSelecionadas);

        } else if ("produtos".equalsIgnoreCase(tipo)) {
            List<Produto> produtosSelecionados = produtoRepository.findAllById(itensIds);

            fornecedor.getProdutos().clear();
            fornecedor.getProdutos().addAll(produtosSelecionados);
        }
        System.out.println("DEBUG: Tipo: " + tipo + " | IDs: " + itensIds);
        fornecedorRepository.save(fornecedor);
    }
}