package Circuit.Circuit.Model;

import lombok.Data;

@Data
public class viaCep {
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private boolean erro;

}
