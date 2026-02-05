package Circuit.Circuit.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordens_servico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "aparelho_id", nullable = false)
    private Aparelho aparelho;

    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ABERTA;

    @Column(name = "senha_dispositivo")
    private String senhaDispositivo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String defeito;

    @Column(name = "estado_conservacao")
    private String estadoConservacao;

    @Column(name = "data_previsao")
    private LocalDate dataPrevisao;

    @Column(name = "data_entrada")
    private LocalDateTime dataEntrada;

    @Column(name = "data_saida")
    private LocalDateTime dataSaida;
    @Column(name = "valor_servico", precision = 10, scale = 2)
    private BigDecimal valorServico = BigDecimal.ZERO;

    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(name = "motivo_desconto", length = 255)
    private String motivoDesconto;

    @Column(name = "porcentagem_desconto")
    private Integer porcentagemDesconto = 0;

    @ManyToMany
    @JoinTable(
            name = "ordem_pecas",
            joinColumns = @JoinColumn(name = "ordem_id"),
            inverseJoinColumns = @JoinColumn(name = "peca_id")
    )
    @ToString.Exclude
    private List<Peca> pecasUtilizadas = new ArrayList<>();
    @PrePersist
    protected void onCreate() {
        if (this.dataEntrada == null) {
            this.dataEntrada = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = Status.ABERTA;
        }
        if (this.valorServico == null) {
            this.valorServico = BigDecimal.ZERO;
        }
        if (this.valorTotal == null) {
            this.valorTotal = BigDecimal.ZERO;
        }
    }
}