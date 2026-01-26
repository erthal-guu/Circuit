package Circuit.Circuit.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "pecas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pecas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "estoque_atual", nullable = false)
    private Integer quantidade;

    @Column(name = "quantidade_minima")
    private Integer quantidadeMinima;

    @Column(name = "preco_compra", precision = 10, scale = 2)
    private BigDecimal precoCompra;

    @Column(name = "preco_venda", precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @ManyToOne
    @JoinColumn(name = "categoria_pecas_id")
    private CategoriaPecas categoria;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "codigo_barras", length = 50)
    private String codigoBarras;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;
}