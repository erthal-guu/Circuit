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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        fornecedorRepository.save(fornecedor);
    }
    @Transactional
    public void desvincularItem(Long fornecedorId, Long itemId, String tipo) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        if ("pecas".equalsIgnoreCase(tipo)) {
            fornecedor.getPecas().removeIf(p -> p.getId().equals(itemId));
        } else if ("produtos".equalsIgnoreCase(tipo)) {
            fornecedor.getProdutos().removeIf(p -> p.getId().equals(itemId));
        }
        fornecedorRepository.save(fornecedor);
    }

    public List<Map<String, Object>> listarItensVinculados(Long fornecedorId, String tipo) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        List<Map<String, Object>> lista = new ArrayList<>();

        if ("pecas".equalsIgnoreCase(tipo)) {
            for (Peca p : fornecedor.getPecas()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", p.getId());
                item.put("nome", p.getNome());
                lista.add(item);
            }
        } else if ("produtos".equalsIgnoreCase(tipo)) {
            for (Produto p : fornecedor.getProdutos()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", p.getId());
                item.put("nome", p.getNome());
                lista.add(item);
            }
        }
        return lista;
    }
}