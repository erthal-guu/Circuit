package Circuit.Circuit.Service;
;
import Circuit.Circuit.Dto.ServicoDto;
import Circuit.Circuit.Model.Peca;
import Circuit.Circuit.Model.Servico;
import Circuit.Circuit.Repository.PecaRepository;
import Circuit.Circuit.Repository.ServicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {
    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private PecaRepository pecaRepository;

    @Transactional
    public Servico cadastrarServico(ServicoDto dto){
        Servico servico = new Servico();
        servico.setNome(dto.nome());
        servico.setValorBase(dto.valorBase());
        if (dto.pecasId() != null && !dto.pecasId().isEmpty()) {

            List<Peca> pecasSelecionadas = pecaRepository.findAllById(dto.pecasId());
            servico.setPecasSugeridas(pecasSelecionadas);
        }
        return servicoRepository.save(servico);
    }

    public List<Servico> ListarAtivos(){
        return servicoRepository.findByAtivoTrue();
    }
    public List<Servico> ListarInativos(){
        return servicoRepository.findByAtivoFalse();
    }
}
