package Circuit.Circuit.Model;

import Circuit.Circuit.Model.Enum.CondicaoPagamento;
import Circuit.Circuit.Model.Enum.FormaPagamento;
import Circuit.Circuit.Model.Enum.StatusVenda;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vendas")
@Getter
@Setter
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusVenda status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="funcionario_id")
    private Funcionario funcionario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="cliente_id")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "condicao_pagamento", nullable = false)
    private CondicaoPagamento condicaoPagamento;

    @Column(name = "numero_parcelas")
    private Integer numeroParcelas;

    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "valor_bruto", precision = 10, scale = 2)
    private BigDecimal valorBruto;

    @Column(name = "porcentagem_desconto", precision = 5, scale = 2)
    private BigDecimal porcentagemDesconto;

    @Column(name = "motivo_desconto")
    private String motivoDesconto;

    @Column(name = "data_venda")
    private LocalDate dataVenda = LocalDate.now();

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemVenda> itens = new ArrayList<>();

}
