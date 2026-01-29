package Circuit.Circuit.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Entity
@Table(name="servicos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "valor_base", nullable = false)
    private BigDecimal valorBase;

    @Column(nullable = false)
    private Boolean ativo;

    @ManyToMany
    @JoinTable(
            name = "servico_peca",
            joinColumns = @JoinColumn(name = "servico_id"),
            inverseJoinColumns = @JoinColumn(name = "peca_id")
    )
    private List<Peca> pecasSugeridas;
}
