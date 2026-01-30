package Circuit.Circuit.Dto;
import java.math.BigDecimal;
import java.util.List;

public record  ServicoDto (String nome, BigDecimal valorBase, Boolean ativo, List<Long> pecasId) {

}
