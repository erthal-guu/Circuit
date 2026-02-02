package Circuit.Circuit.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "funcionarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(length = 15)
    private String telefone;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Cargo cargo;

    @Column(length = 9)
    private String cep;

    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;

    @Column(nullable = false)
    private Boolean ativo;
    @Column(name = "data_admissao")
    private LocalDate dataAdmissao;

}
