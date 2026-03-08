package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Fornecedor;
import Circuit.Circuit.Model.Peca;
import Circuit.Circuit.Model.Produto;
import Circuit.Circuit.Repository.FornecedorRepository;
import Circuit.Circuit.Repository.PecaRepository;
import Circuit.Circuit.Repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FornecedorService {

    private static final Logger logger = LoggerFactory.getLogger(FornecedorService.class);

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PecaRepository pecaRepository;

    public Fornecedor cadastrar(Fornecedor fornecedor) {
        logger.info("Tentativa de cadastro de novo fornecedor - Razão Social: {}, CNPJ: {}, Tipo: {}",
                fornecedor.getRazaoSocial(), fornecedor.getCnpj(), fornecedor.getTipo());

        if (fornecedorRepository.existsByCnpj(fornecedor.getCnpj())) {
            logger.warn("Falha ao cadastrar fornecedor: CNPJ já existe - {}", fornecedor.getCnpj());
            throw new RuntimeException("Este CNPJ já está cadastrado no sistema.");
        }

        Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);
        logger.info("Fornecedor cadastrado com sucesso - ID: {}, Razão Social: {}, CNPJ: {}",
                fornecedorSalvo.getId(), fornecedorSalvo.getRazaoSocial(), fornecedorSalvo.getCnpj());

        return fornecedorSalvo;
    }

    public List<Fornecedor> listarFornecedoresAtivos() {
        List<Fornecedor> fornecedores = fornecedorRepository.findByAtivoTrueOrderById();
        logger.debug("Listagem de fornecedores ativos - {} registros encontrados", fornecedores.size());
        return fornecedores;
    }

    public List<Fornecedor> listarFornecedoresInativos() {
        List<Fornecedor> fornecedores = fornecedorRepository.findByAtivoFalseOrderById();
        logger.debug("Listagem de fornecedores inativos - {} registros encontrados", fornecedores.size());
        return fornecedores;
    }

    public Fornecedor excluirFornecedor(Long id) {
        Fornecedor fornecedor = fornecedorRepository.getReferenceById(id);
        logger.info("Tentativa de exclusão de fornecedor - ID: {}, Razão Social: {}, CNPJ: {}",
                fornecedor.getId(), fornecedor.getRazaoSocial(), fornecedor.getCnpj());

        fornecedor.setAtivo(false);
        Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);

        logger.info("Fornecedor desativado com sucesso - ID: {}, Razão Social: {}, CNPJ: {}",
                fornecedorSalvo.getId(), fornecedorSalvo.getRazaoSocial(), fornecedorSalvo.getCnpj());

        return fornecedorSalvo;
    }

    public Fornecedor restaurarFornecedor(Long id) {
        Fornecedor fornecedor = fornecedorRepository.getReferenceById(id);
        logger.info("Tentativa de restauração de fornecedor - ID: {}, Razão Social: {}, CNPJ: {}",
                fornecedor.getId(), fornecedor.getRazaoSocial(), fornecedor.getCnpj());

        fornecedor.setAtivo(true);
        Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);

        logger.info("Fornecedor restaurado com sucesso - ID: {}, Razão Social: {}, CNPJ: {}",
                fornecedorSalvo.getId(), fornecedorSalvo.getRazaoSocial(), fornecedorSalvo.getCnpj());

        return fornecedorSalvo;
    }

    public Fornecedor editarFornecedor(Long id, Fornecedor dadosAtualizados) {
        Fornecedor fornecedor = fornecedorRepository.getReferenceById(id);
        logger.info("Tentativa de edição de fornecedor - ID: {}, Razão Social atual: {}, Razão Social nova: {}",
                id, fornecedor.getRazaoSocial(), dadosAtualizados.getRazaoSocial());

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

        Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);

        logger.info("Fornecedor editado com sucesso - ID: {}, Razão Social: {}, CNPJ: {}",
                fornecedorSalvo.getId(), fornecedorSalvo.getRazaoSocial(), fornecedorSalvo.getCnpj());

        return fornecedorSalvo;
    }
    @Transactional
    public void vincularItens(Long fornecedorId, List<Long> itensIds, String tipo) {
        logger.info("Vinculando itens ao fornecedor - Fornecedor ID: {}, Tipo: {}, Quantidade de itens: {}",
                fornecedorId, tipo, itensIds != null ? itensIds.size() : 0);

        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        if ("PECA".equalsIgnoreCase(tipo)) {
            List<Peca> pecasSelecionadas = pecaRepository.findAllById(itensIds);

            fornecedor.getPecas().clear();
            fornecedor.getPecas().addAll(pecasSelecionadas);

            logger.info("Peças vinculadas ao fornecedor - Fornecedor: {}, Quantidade: {}",
                    fornecedorId, pecasSelecionadas.size());

        } else if ("PRODUTO".equalsIgnoreCase(tipo)) {
            List<Produto> produtosSelecionados = produtoRepository.findAllById(itensIds);

            fornecedor.getProdutos().clear();
            fornecedor.getProdutos().addAll(produtosSelecionados);

            logger.info("Produtos vinculados ao fornecedor - Fornecedor: {}, Quantidade: {}",
                    fornecedorId, produtosSelecionados.size());
        }
        fornecedorRepository.save(fornecedor);
    }
    @Transactional
    public void desvincularItem(Long fornecedorId, Long itemId, String tipo) {
        logger.info("Desvinculando item do fornecedor - Fornecedor ID: {}, Item ID: {}, Tipo: {}",
                fornecedorId, itemId, tipo);

        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        if ("PECA".equalsIgnoreCase(tipo)) {
            fornecedor.getPecas().removeIf(p -> p.getId().equals(itemId));
            logger.info("Peça desvinculada do fornecedor - Fornecedor: {}, Peça ID: {}",
                    fornecedorId, itemId);
        } else if ("PRODUTO".equalsIgnoreCase(tipo)) {
            fornecedor.getProdutos().removeIf(p -> p.getId().equals(itemId));
            logger.info("Produto desvinculado do fornecedor - Fornecedor: {}, Produto ID: {}",
                    fornecedorId, itemId);
        }
        fornecedorRepository.save(fornecedor);
    }

    @Transactional
    public List<Map<String, Object>> listarItensVinculados(Long fornecedorId, String tipo) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        List<Map<String, Object>> lista = new ArrayList<>();

        logger.debug("Listando itens vinculados ao fornecedor - Fornecedor ID: {}, Tipo: {}",
                fornecedorId, tipo);

        if ("PECA".equalsIgnoreCase(tipo)) {
            for (Peca p : fornecedor.getPecas()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", p.getId());
                item.put("nome", p.getNome());
                item.put("precoCompra", p.getPrecoCompra());
                lista.add(item);
            }
            logger.debug("Peças listadas - Quantidade: {}", lista.size());
        } else if ("PRODUTO".equalsIgnoreCase(tipo)) {
            for (Produto p : fornecedor.getProdutos()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", p.getId());
                item.put("nome", p.getNome());
                item.put("precoCompra", p.getPrecoCompra());
                lista.add(item);
            }
            logger.debug("Produtos listados - Quantidade: {}", lista.size());
        }
        return lista;
    }
}