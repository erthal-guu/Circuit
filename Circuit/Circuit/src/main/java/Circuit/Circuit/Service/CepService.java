package Circuit.Circuit.Service;

import Circuit.Circuit.ApiDto.viaCep;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CepService {
    public viaCep consultarCep(String cep){
        String url = "https://viacep.com.br/ws/" + cep.replaceAll("\\D", "") + "/json/";
        RestTemplate restTemplate = new RestTemplate();
        viaCep dto = restTemplate.getForObject(url, viaCep.class);

        if (dto == null || dto.isErro()) throw new RuntimeException("CEP inválido!");
        return dto;
    }
}
