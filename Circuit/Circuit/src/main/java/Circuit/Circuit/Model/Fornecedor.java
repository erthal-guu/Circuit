package Circuit.Circuit.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fornecedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_fantasia", nullable = false, length = 150)
    private String nomeFantasia;

    @Column(name = "razao_social", nullable = false, length = 150)
    private String razaoSocial;

    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(length = 15)
    private String telefone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean ativo = true;


    @Column(length = 9)
    private String cep;

    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;

}