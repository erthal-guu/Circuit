package Circuit.Circuit.Dto;

import java.math.BigDecimal;

public class DistribuicaoDespesasDto {
    private String categoria;
    private BigDecimal total;

    public DistribuicaoDespesasDto() {
    }

    public DistribuicaoDespesasDto(String categoria, BigDecimal total) {
        this.categoria = categoria;
        this.total = total;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
