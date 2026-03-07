package Circuit.Circuit.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContaPagarDashboardDto {
    private Long id;
    private String nomeFornecedor;
    private String descricao;
    private String origem;
    private Long origemId;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private String status;
    private String iniciais;

    public ContaPagarDashboardDto() {
    }

    public ContaPagarDashboardDto(Long id, String nomeFornecedor, String descricao, String origem, Long origemId, 
                                   BigDecimal valor, LocalDate dataVencimento, String status, String iniciais) {
        this.id = id;
        this.nomeFornecedor = nomeFornecedor;
        this.descricao = descricao;
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

    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
