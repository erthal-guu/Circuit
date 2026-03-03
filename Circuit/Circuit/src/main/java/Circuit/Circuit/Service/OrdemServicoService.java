package Circuit.Circuit.Service;

import Circuit.Circuit.Model.OrdemServico;
import Circuit.Circuit.Model.StatusOrdem;
import Circuit.Circuit.Repository.OrdemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class OrdemServicoService {
    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    public OrdemServico cadastrar(OrdemServico ordemServico){
        return ordemServicoRepository.save(ordemServico);
    }
    public List<OrdemServico> ListarOrdens(){
        return ordemServicoRepository.findAll();
    }

    public List<OrdemServico> ListarOrdensAbertas(){
        List<StatusOrdem> statusAtivos = Arrays.asList(
                StatusOrdem.ABERTA,
                StatusOrdem.EM_ANALISE,
                StatusOrdem.AGUARDANDO_APROVACAO,
                StatusOrdem.EM_REPARO,
                StatusOrdem.AGUARDANDO_PECA
        );
        return ordemServicoRepository.findByStatusIn(statusAtivos);
    }
    public List<OrdemServico> ListarOrdensFinalizadas(){
        List<StatusOrdem> statusEncerrados= Arrays.asList(
            StatusOrdem.FINALIZADA,
            StatusOrdem.CANCELADA
        );
    return ordemServicoRepository.findByStatusIn(statusEncerrados);
    }

    public OrdemServico editar(Long id, OrdemServico ordemAtualizada) {
        OrdemServico ordemBanco = ordemServicoRepository.findById(id).orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada"));


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
        }
        else if (ordemAtualizada.getStatus() != StatusOrdem.FINALIZADA) {
            ordemBanco.setDataSaida(null);
        }
        return ordemServicoRepository.save(ordemBanco);
    }
    public void atualizarStatus(Long id, StatusOrdem novoStatus){
        OrdemServico ordemServico = ordemServicoRepository.findById(id).get();
        if (novoStatus == StatusOrdem.FINALIZADA) {
            ordemServico.setDataSaida(LocalDateTime.now());
        } else {
            ordemServico.setDataSaida(null);
        }
        ordemServico.setStatus(novoStatus);
        ordemServicoRepository.save(ordemServico);
        }
    }

