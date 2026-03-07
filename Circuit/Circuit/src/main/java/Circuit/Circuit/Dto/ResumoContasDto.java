package Circuit.Circuit.Dto;

import java.math.BigDecimal;
import java.util.List;

public class ResumoContasDto {
    private Integer pendentes;
    private Integer vencidas;
    private BigDecimal total;
    private List<?> contas;

    public ResumoContasDto() {
    }

    public ResumoContasDto(Integer pendentes, Integer vencidas, BigDecimal total, List<?> contas) {
        this.pendentes = pendentes;
        this.vencidas = vencidas;
        this.total = total;
        this.contas = contas;
    }

    public Integer getPendentes() {
        return pendentes;
    }

    public void setPendentes(Integer pendentes) {
        this.pendentes = pendentes;
    }

    public Integer getVencidas() {
        return vencidas;
    }

    public void setVencidas(Integer vencidas) {
        this.vencidas = vencidas;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<?> getContas() {
        return contas;
    }

    public void setContas(List<?> contas) {
        this.contas = contas;
    }
}
