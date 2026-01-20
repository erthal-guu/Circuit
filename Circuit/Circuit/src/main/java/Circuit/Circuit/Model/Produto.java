package Circuit.Circuit.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private Long codigoBarras;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;
}