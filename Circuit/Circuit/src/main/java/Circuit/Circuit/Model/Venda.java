package Circuit.Circuit.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vendas")
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="funcionario_id")
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name="cliente_id")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento")
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "condicao_pagamento")
    private CondicaoPagamento condicaoPagamento;

    @Column(name = "numero_parcelas")
    private Integer numeroParcelas;

    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "porcentagem_desconto", precision = 5, scale = 2)
    private BigDecimal porcentagemDesconto;

    @Column(name = "motivo_desconto")
    private String motivoDesconto;

    @Column(name = "data_venda")
    private LocalDate dataVenda;

}
