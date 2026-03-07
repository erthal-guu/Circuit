package Circuit.Circuit.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContaReceberDashboardDto {
    private Long id;
    private String nomeCliente;
    private String origem;
    private Long origemId;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private String status;
    private String iniciais;

    public ContaReceberDashboardDto() {
    }

    public ContaReceberDashboardDto(Long id, String nomeCliente, String origem, Long origemId, 
                                     BigDecimal valor, LocalDate dataVencimento, String status, String iniciais) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.origem = origem;
        this.origemId = origemId;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.status = status;
        this.iniciais = iniciais;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public Long getOrigemId() {
        return origemId;
    }

    public void setOrigemId(Long origemId) {
        this.origemId = origemId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIniciais() {
        return iniciais;
    }

    public void setIniciais(String iniciais) {
        this.iniciais = iniciais;
    }
}
