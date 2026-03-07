package Circuit.Circuit.Dto;

import java.math.BigDecimal;

public class FluxoCaixaMensalDto {
    private String mes;
    private BigDecimal receitas;
    private BigDecimal despesas;

    public FluxoCaixaMensalDto() {
    }

    public FluxoCaixaMensalDto(String mes, BigDecimal receitas, BigDecimal despesas) {
        this.mes = mes;
        this.receitas = receitas;
        this.despesas = despesas;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public BigDecimal getReceitas() {
        return receitas;
    }

    public void setReceitas(BigDecimal receitas) {
        this.receitas = receitas;
    }

    public BigDecimal getDespesas() {
        return despesas;
    }

    public void setDespesas(BigDecimal despesas) {
        this.despesas = despesas;
    }
}
