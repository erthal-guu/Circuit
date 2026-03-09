package Circuit.Circuit.Service;

import Circuit.Circuit.Repository.RelatorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private RelatorioRepository relatorioRepository;

    public List<Map<String, Object>> getRelatorioVendas(LocalDate inicio, LocalDate fim) {
        List<Object[]> resultados = relatorioRepository.findTopFuncionarios(inicio, fim);
        return resultados.stream().map(linha -> {
            Map<String, Object> map = new HashMap<>();
            map.put("Funcionário", linha[0] != null ? linha[0].toString() : "N/A");
            map.put("Total Vendido", linha[1] != null ? "R$ " + linha[1].toString() : "R$ 0,00");
            map.put("Qtd Vendas", linha[2] != null ? linha[2].toString() : "0");
            return map;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getRelatorioEstoque(LocalDate inicio, LocalDate fim) {
        List<Object[]> resultados = relatorioRepository.findProdutosMaisUtilizados(inicio, fim);
        return resultados.stream().map(linha -> {
            Map<String, Object> map = new HashMap<>();
            map.put("Produto/Peça", linha[0] != null ? linha[0].toString() : "N/A");
            map.put("Quantidade", linha[1] != null ? linha[1].toString() : "0");
            return map;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getRelatorioFinanceiro(LocalDate inicio, LocalDate fim) {
        List<Object[]> resultados = relatorioRepository.findClientesMaisComprasFinanceiro(inicio, fim);
        return resultados.stream().map(linha -> {
            Map<String, Object> map = new HashMap<>();
            map.put("Cliente", linha[0] != null ? linha[0].toString() : "N/A");
            map.put("Valor em Aberto", linha[1] != null ? "R$ " + linha[1].toString() : "R$ 0,00");
            return map;
        }).collect(Collectors.toList());
    }
}