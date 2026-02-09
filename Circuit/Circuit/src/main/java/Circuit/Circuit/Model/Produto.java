package Circuit.Circuit.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"fornecedor", "categoria"})
@ToString(exclude = {"fornecedor", "categoria"})
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "quantidade_minima")
    private Integer quantidadeMinima;

    @Column(name = "preco_compra", nullable = false)
    private BigDecimal precoCompra;

    @Column(name = "preco_venda", nullable = false)
    private BigDecimal precoVenda;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "codigo_barras", nullable = false)
    private String codigoBarras;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;
}