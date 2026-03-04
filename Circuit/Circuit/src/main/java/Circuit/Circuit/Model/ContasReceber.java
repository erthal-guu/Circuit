package Circuit.Circuit.Model;

import Circuit.Circuit.Model.Enum.FormaPagamento;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "contas_a_receber")
public class ContasReceber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal valor;

    private BigDecimal valorRecebido;

    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    private StatusFinanceiro status;

    private String origem;
    private Long origemId;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
