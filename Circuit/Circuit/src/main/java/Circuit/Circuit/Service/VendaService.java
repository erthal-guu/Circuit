package Circuit.Circuit.Service;

import Circuit.Circuit.Model.ItemVenda;
import Circuit.Circuit.Model.Produto;
import Circuit.Circuit.Model.Venda;
import Circuit.Circuit.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class VendaService {

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

    @Transactional
    public void salvarVenda(Long id, Long clienteId, Long funcionarioId, BigDecimal valorTotal,
                            String motivoDesconto, LocalDate dataVenda,
                            List<Long> itensId, List<Integer> quantidadeItens) {

        Venda venda = new Venda();
        venda.setCliente(clienteRepository.findById(clienteId).orElseThrow());
        venda.setFuncionario(funcionarioRepository.findById(funcionarioId).orElseThrow());
        venda.setValorTotal(valorTotal);
        venda.setMotivoDesconto(motivoDesconto);
        venda.setDataVenda(dataVenda);
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

        vendaRepository.save(venda);
    }
}

