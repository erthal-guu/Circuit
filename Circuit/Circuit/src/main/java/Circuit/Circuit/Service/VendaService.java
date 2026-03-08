package Circuit.Circuit.Service;

import Circuit.Circuit.Model.*;
import Circuit.Circuit.Model.Enum.CondicaoPagamento;
import Circuit.Circuit.Model.Enum.FormaPagamento;
import Circuit.Circuit.Model.Enum.StatusVenda;
import Circuit.Circuit.Repository.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class VendaService {

    private static final Logger logger = LoggerFactory.getLogger(VendaService.class);

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ItemVendaRepository itemVendaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private ContaReceberService contaReceberService;

    @Transactional
    public void salvarVenda(Long id, Long clienteId, Long funcionarioId, BigDecimal valorTotal, BigDecimal valorBruto,
                            BigDecimal porcentagemDesconto,
                            String motivoDesconto, LocalDate dataVenda, String codigo,
                            StatusVenda status, FormaPagamento formaPagamento,
                            CondicaoPagamento condicaoPagamento, Integer numeroParcelas,
                            List<Long> itensId, List<Integer> quantidadeItens) {

        logger.info("Tentativa de salvar venda - Código: {}, Valor Total: {}, Status: {}, Cliente ID: {}, Funcionário ID: {}",
                codigo, valorTotal, status, clienteId, funcionarioId);
        Venda venda = new Venda();
        venda.setCodigo(codigo);
        venda.setStatus(status);
        venda.setCliente(clienteRepository.findById(clienteId).orElseThrow());
        venda.setFuncionario(funcionarioRepository.findById(funcionarioId).orElseThrow());
        venda.setValorTotal(valorTotal);
        venda.setMotivoDesconto(motivoDesconto);
        venda.setValorBruto(valorBruto);
        venda.setPorcentagemDesconto(porcentagemDesconto);
        venda.setDataVenda(dataVenda);
        venda.setFormaPagamento(formaPagamento);
        venda.setCondicaoPagamento(condicaoPagamento);
        venda.setNumeroParcelas(numeroParcelas);
        for (int i = 0; i < itensId.size(); i++) {
            Long produtoId = itensId.get(i);
            Integer qtd = quantidadeItens.get(i);
            ItemVenda item = new ItemVenda();
            Produto produto = produtoRepository.findById(produtoId).orElseThrow();

            item.setProduto(produto);
            item.setQuantidade(qtd);
            item.setVenda(venda);
            estoqueService.retirarEstoqueProd(produtoId, qtd);

            venda.getItens().add(item);
        }

        Venda vendaSalva = vendaRepository.save(venda);
        logger.info("Venda salva com sucesso - ID: {}, Código: {}, Valor Total: {}",
                vendaSalva.getId(), vendaSalva.getCodigo(), vendaSalva.getValorTotal());

        if (vendaSalva.getStatus() == StatusVenda.CONCLUIDA){
            contaReceberService.gerarContaReceberParaVenda(vendaSalva);
        }
    }

    @Transactional
    public List<Venda> listarTodasVendas(){
        List<Venda> vendas = vendaRepository.findAll();
        logger.debug("Listagem de todas as vendas - {} registros encontrados", vendas.size());
        return vendas;
    }

    @Transactional
    public List<Venda> listarVendasPendentes(){
        List<Venda> vendas = vendaRepository.findByStatus(StatusVenda.PENDENTE);
        logger.debug("Listagem de vendas pendentes - {} registros encontrados", vendas.size());
        return vendas;
    }

    @Transactional
    public List<Venda> listarVendasConcluidas(){
        List<Venda> vendas = vendaRepository.findByStatus(StatusVenda.CONCLUIDA);
        logger.debug("Listagem de vendas concluídas - {} registros encontrados", vendas.size());
        return vendas;
    }

    public void atualizarStatus(Long id, StatusVenda novoStatus){
        Venda venda = vendaRepository.findById(id).orElseThrow();
        logger.info("Tentativa de atualizar status de venda - ID: {}, Status atual: {}, Novo status: {}",
                id, venda.getStatus(), novoStatus);
        venda.setStatus(novoStatus);
        if (novoStatus == StatusVenda.CONCLUIDA) {
            contaReceberService.gerarContaReceberParaVenda(venda);
        }
        Venda vendaSalva = vendaRepository.save(venda);
        logger.info("Status de venda atualizado com sucesso - ID: {}, Novo status: {}", vendaSalva.getId(), vendaSalva.getStatus());
    }

    public void atualizarVenda(Long id, Long clienteId, Long funcionarioId, BigDecimal valorTotal, BigDecimal valorBruto,
                               BigDecimal porcentagemDesconto,
                               String motivoDesconto, LocalDate dataVenda, String codigo,
                               StatusVenda status, FormaPagamento formaPagamento,
                               CondicaoPagamento condicaoPagamento, Integer numeroParcelas,
                               List<Long> itensId, List<Integer> quantidadeItens) {
        Venda venda = vendaRepository.findById(id).orElseThrow();

        if (venda.getStatus() != StatusVenda.PENDENTE) {
            logger.warn("Tentativa de editar venda com status diferente de PENDENTE - ID: {}, Status atual: {}", id, venda.getStatus());
            throw new RuntimeException("Só é possível editar vendas com status PENDENTE");
        }

        logger.info("Tentativa de editar venda - ID: {}, Código: {}, Valor Total: {}", id, codigo, valorTotal);
        venda.setCodigo(codigo);
        venda.setStatus(status);
        venda.setCliente(clienteRepository.findById(clienteId).orElseThrow());
        venda.setFuncionario(funcionarioRepository.findById(funcionarioId).orElseThrow());
        venda.setValorTotal(valorTotal);
        venda.setMotivoDesconto(motivoDesconto);
        venda.setValorBruto(valorBruto);
        venda.setPorcentagemDesconto(porcentagemDesconto);
        venda.setDataVenda(dataVenda);
        venda.setFormaPagamento(formaPagamento);
        venda.setCondicaoPagamento(condicaoPagamento);
        venda.setNumeroParcelas(numeroParcelas);

        venda.getItens().clear();
        for (int i = 0; i < itensId.size(); i++) {
            Long produtoId = itensId.get(i);
            Integer qtd = quantidadeItens.get(i);
            ItemVenda item = new ItemVenda();
            Produto produto = produtoRepository.findById(produtoId).orElseThrow();

            item.setProduto(produto);
            item.setQuantidade(qtd);
            item.setVenda(venda);
            estoqueService.retirarEstoqueProd(produtoId, qtd);

            venda.getItens().add(item);
        }

        Venda vendaSalva = vendaRepository.save(venda);
        logger.info("Venda editada com sucesso - ID: {}, Código: {}", vendaSalva.getId(), vendaSalva.getCodigo());
    }
}

