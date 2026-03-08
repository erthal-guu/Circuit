package Circuit.Circuit.Service;

import Circuit.Circuit.Dto.ServicoDto;
import Circuit.Circuit.Model.Peca;
import Circuit.Circuit.Model.Servico;
import Circuit.Circuit.Repository.PecaRepository;
import Circuit.Circuit.Repository.ServicoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    private static final Logger logger = LoggerFactory.getLogger(ServicoService.class);

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private PecaRepository pecaRepository;

    @Transactional
    public Servico cadastrar(ServicoDto dto){
        logger.info("Tentativa de cadastro de novo serviço - Nome: {}, Valor Base: {}, Peças: {}",
                dto.nome(), dto.valorBase(), dto.pecasId() != null ? dto.pecasId().size() + " peças" : "Nenhuma");
        Servico servico = new Servico();
        servico.setNome(dto.nome());
        servico.setValorBase(dto.valorBase());
        if (dto.pecasId() != null && !dto.pecasId().isEmpty()) {
            servico.setAtivo(dto.ativo() != null ? dto.ativo() : true);

            List<Peca> pecasSelecionadas = pecaRepository.findAllById(dto.pecasId());
            servico.setPecasSugeridas(pecasSelecionadas);
        }
        Servico servicoSalvo = servicoRepository.save(servico);
        logger.info("Serviço cadastrado com sucesso - ID: {}, Nome: {}, Valor Base: {}",
                servicoSalvo.getId(), servicoSalvo.getNome(), servicoSalvo.getValorBase());
        return servicoSalvo;
    }

    public List<Servico> listarAtivos(){
        List<Servico> servicos = servicoRepository.findByAtivoTrue();
        logger.debug("Listagem de serviços ativos - {} registros encontrados", servicos.size());
        return servicos;
    }

    public List<Servico> listarInativos(){
        List<Servico> servicos = servicoRepository.findByAtivoFalse();
        logger.debug("Listagem de serviços inativos - {} registros encontrados", servicos.size());
        return servicos;
    }

    public void excluir(Long id){
        Servico servico = servicoRepository.getReferenceById(id);
        logger.info("Tentativa de exclusão de serviço - ID: {}, Nome: {}, Valor Base: {}",
                servico.getId(), servico.getNome(), servico.getValorBase());
        servico.setAtivo(false);
        servicoRepository.save(servico);
        logger.info("Serviço desativado com sucesso - ID: {}, Nome: {}", servico.getId(), servico.getNome());
    }

    public void restaurar(Long id){
        Servico servico = servicoRepository.getReferenceById(id);
        logger.info("Tentativa de restauração de serviço - ID: {}, Nome: {}, Valor Base: {}",
                servico.getId(), servico.getNome(), servico.getValorBase());
        servico.setAtivo(true);
        servicoRepository.save(servico);
        logger.info("Serviço restaurado com sucesso - ID: {}, Nome: {}", servico.getId(), servico.getNome());
    }

    @Transactional
    public Servico editarServico(Long id, ServicoDto dadosAtualizados) {
        Servico servicoEditar = servicoRepository.getReferenceById(id);
        logger.info("Tentativa de edição de serviço - ID: {}, Nome atual: {}, Nome novo: {}, Valor novo: {}",
                id, servicoEditar.getNome(), dadosAtualizados.nome(), dadosAtualizados.valorBase());
        servicoEditar.setNome(dadosAtualizados.nome());
        servicoEditar.setValorBase(dadosAtualizados.valorBase());
        servicoEditar.setAtivo(dadosAtualizados.ativo() != null ? dadosAtualizados.ativo() : servicoEditar.getAtivo());
        if (dadosAtualizados.pecasId() != null) {
            List<Peca> novasPecas = pecaRepository.findAllById(dadosAtualizados.pecasId());
            servicoEditar.getPecasSugeridas().clear();
            servicoEditar.getPecasSugeridas().addAll(novasPecas);
            logger.debug("Peças associadas ao serviço atualizadas - {} peças", novasPecas.size());
        } else {
            servicoEditar.getPecasSugeridas().clear();
            logger.debug("Peças associadas ao serviço removidas");
        }

        Servico servicoSalvo = servicoRepository.save(servicoEditar);
        logger.info("Serviço editado com sucesso - ID: {}, Nome: {}, Valor Base: {}",
                servicoSalvo.getId(), servicoSalvo.getNome(), servicoSalvo.getValorBase());
        return servicoSalvo;
    }
}
