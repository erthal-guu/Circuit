package Circuit.Circuit.Model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "pedidos_compra")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Enumerated(EnumType.STRING)
    private StatusPedido status = StatusPedido.RASCUNHO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Funcionario responsavel;

    private LocalDate dataPedido;
    private LocalDate dataPrevisaoEntrega;

    private BigDecimal valorSubtotal = BigDecimal.ZERO;
    private BigDecimal valorFrete = BigDecimal.ZERO;
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String observacoes;
}
