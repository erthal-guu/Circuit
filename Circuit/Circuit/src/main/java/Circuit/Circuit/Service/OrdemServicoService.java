package Circuit.Circuit.Service;

import Circuit.Circuit.Model.OrdemServico;
import Circuit.Circuit.Model.Enum.StatusOrdem;
import Circuit.Circuit.Repository.OrdemServicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class OrdemServicoService {

    private static final Logger logger = LoggerFactory.getLogger(OrdemServicoService.class);

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private ContaReceberService contaReceberService;

    public OrdemServico cadastrar(OrdemServico ordemServico){
        logger.info("Tentativa de cadastro de ordem de serviço - Cliente: {}, Aparelho: {}, Status: {}",
                ordemServico.getCliente() != null ? ordemServico.getCliente().getNome() : "N/A",
                ordemServico.getAparelho() != null ? ordemServico.getAparelho().getModelo() : "N/A",
                ordemServico.getStatus());
        OrdemServico ordemSalva = ordemServicoRepository.save(ordemServico);
        if (ordemSalva.getStatus() == StatusOrdem.FINALIZADA) {
            contaReceberService.gerarContaReceberParaOS(ordemSalva);
        }
        logger.info("Ordem de serviço cadastrada com sucesso - ID: {}, Cliente: {}, Status: {}",
                ordemSalva.getId(),
                ordemSalva.getCliente() != null ? ordemSalva.getCliente().getNome() : "N/A",
                ordemSalva.getStatus());
        return ordemSalva;
    }

    public List<OrdemServico> listarOrdens(){
        List<OrdemServico> ordens = ordemServicoRepository.findAll();
        logger.debug("Listagem de todas as ordens de serviço - {} registros encontrados", ordens.size());
        return ordens;
    }

    public List<OrdemServico> listarOrdensAbertas(){
        List<StatusOrdem> statusAtivos = Arrays.asList(
                StatusOrdem.ABERTA,
                StatusOrdem.EM_ANALISE,
                StatusOrdem.AGUARDANDO_APROVACAO,
                StatusOrdem.EM_REPARO,
                StatusOrdem.AGUARDANDO_PECA
        );
        List<OrdemServico> ordens = ordemServicoRepository.findByStatusIn(statusAtivos);
        logger.debug("Listagem de ordens de serviço abertas - {} registros encontrados", ordens.size());
        return ordens;
    }

    public List<OrdemServico> listarOrdensFinalizadas(){
        List<StatusOrdem> statusEncerrados = Arrays.asList(
            StatusOrdem.FINALIZADA,
            StatusOrdem.CANCELADA
        );
        List<OrdemServico> ordens = ordemServicoRepository.findByStatusIn(statusEncerrados);
        logger.debug("Listagem de ordens de serviço finalizadas - {} registros encontrados", ordens.size());
        return ordens;
    }

    public OrdemServico editar(Long id, OrdemServico ordemAtualizada) {
        OrdemServico ordemBanco = ordemServicoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Ordem de serviço não encontrada - ID: {}", id);
                    return new RuntimeException("Ordem de serviço não encontrada");
                });

        logger.info("Tentativa de edição de ordem de serviço - ID: {}, Cliente: {}, Status atual: {}, Novo status: {}",
                id, ordemBanco.getCliente() != null ? ordemBanco.getCliente().getNome() : "N/A",
                ordemBanco.getStatus(), ordemAtualizada.getStatus());
        ordemBanco.setFuncionario(ordemAtualizada.getFuncionario());
        ordemBanco.setServico(ordemAtualizada.getServico());
        ordemBanco.setDefeito(ordemAtualizada.getDefeito());
        ordemBanco.setEstadoConservacao(ordemAtualizada.getEstadoConservacao());
        ordemBanco.setSenhaDispositivo(ordemAtualizada.getSenhaDispositivo());
        ordemBanco.setDataPrevisao(ordemAtualizada.getDataPrevisao());
        ordemBanco.setValorServico(ordemAtualizada.getValorServico());
        ordemBanco.setValorTotal(ordemAtualizada.getValorTotal());
        ordemBanco.setPecasUtilizadas(ordemAtualizada.getPecasUtilizadas());
        if (ordemAtualizada.getStatus() == StatusOrdem.FINALIZADA && ordemBanco.getDataSaida() == null) {
            ordemBanco.setDataSaida(LocalDateTime.now());
            contaReceberService.gerarContaReceberParaOS(ordemBanco);
        }
        else if (ordemAtualizada.getStatus() != StatusOrdem.FINALIZADA) {
            ordemBanco.setDataSaida(null);
        }
        OrdemServico ordemSalva = ordemServicoRepository.save(ordemBanco);
        logger.info("Ordem de serviço editada com sucesso - ID: {}, Status: {}", ordemSalva.getId(), ordemSalva.getStatus());
        return ordemSalva;
    }

    public void atualizarStatus(Long id, StatusOrdem novoStatus){
        OrdemServico ordemServico = ordemServicoRepository.findById(id).get();
        logger.info("Tentativa de atualizar status de ordem de serviço - ID: {}, Status atual: {}, Novo status: {}",
                id, ordemServico.getStatus(), novoStatus);
        if (novoStatus == StatusOrdem.FINALIZADA) {
            ordemServico.setDataSaida(LocalDateTime.now());
            contaReceberService.gerarContaReceberParaOS(ordemServico);
        } else {
            ordemServico.setDataSaida(null);
        }
        ordemServico.setStatus(novoStatus);
        OrdemServico ordemSalva = ordemServicoRepository.save(ordemServico);
        logger.info("Status de ordem de serviço atualizado com sucesso - ID: {}, Novo status: {}", ordemSalva.getId(), ordemSalva.getStatus());
    }
}

