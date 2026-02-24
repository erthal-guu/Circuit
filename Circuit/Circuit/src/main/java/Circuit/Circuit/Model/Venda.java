package Circuit.Circuit.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "vendas")
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
