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
    public Servico cadastrar(ServicoDto dto){
        Servico servico = new Servico();
        servico.setNome(dto.nome());
        servico.setValorBase(dto.valorBase());
        if (dto.pecasId() != null && !dto.pecasId().isEmpty()) {
            servico.setAtivo(dto.ativo() != null ? dto.ativo() : true);

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

    public void excluir (Long id){
       Servico servico= servicoRepository.getReferenceById(id);
        servico.setAtivo(false);
        servicoRepository.save(servico);

    }
    public void restaurar(Long id){
        Servico servico = servicoRepository.getReferenceById(id);
        servico.setAtivo(true);
        servicoRepository.save(servico);
    }
    @Transactional
    public Servico editarServico(Long id, ServicoDto dadosAtualizados) {
        Servico servicoEditar = servicoRepository.getReferenceById(id);
        servicoEditar.setNome(dadosAtualizados.nome());
        servicoEditar.setValorBase(dadosAtualizados.valorBase());
        servicoEditar.setAtivo(dadosAtualizados.ativo() != null ? dadosAtualizados.ativo() : servicoEditar.getAtivo());
        if (dadosAtualizados.pecasId() != null) {
            List<Peca> novasPecas = pecaRepository.findAllById(dadosAtualizados.pecasId());
            servicoEditar.getPecasSugeridas().clear();
            servicoEditar.getPecasSugeridas().addAll(novasPecas);
        } else {
            servicoEditar.getPecasSugeridas().clear();
        }

        return servicoRepository.save(servicoEditar);
    }
}
