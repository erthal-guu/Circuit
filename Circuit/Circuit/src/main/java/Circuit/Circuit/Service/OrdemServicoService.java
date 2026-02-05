package Circuit.Circuit.Service;

import Circuit.Circuit.Model.OrdemServico;
import Circuit.Circuit.Model.Status;
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
        List<Status> statusAtivos = Arrays.asList(
                Status.ABERTA,
                Status.EM_ANALISE,
                Status.AGUARDANDO_APROVACAO,
                Status.EM_REPARO,
                Status.AGUARDANDO_PECA
        );
        return ordemServicoRepository.findByStatusIn(statusAtivos);
    }
    public List<OrdemServico> ListarOrdensFinalizadas(){
        List<Status> statusEncerrados= Arrays.asList(
            Status.FINALIZADA,
            Status.CANCELADA
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
        if (ordemAtualizada.getStatus() == Status.FINALIZADA && ordemBanco.getDataSaida() == null) {
            ordemBanco.setDataSaida(LocalDateTime.now());
        }
        else if (ordemAtualizada.getStatus() != Status.FINALIZADA) {
            ordemBanco.setDataSaida(null);
        }
        ordemBanco.setStatus(ordemAtualizada.getStatus());
        return ordemServicoRepository.save(ordemBanco);
    }
    public void atualizarStatus(Long id, Status novoStatus){
        OrdemServico ordemServico = ordemServicoRepository.findById(id).get();
        if (novoStatus == Status.FINALIZADA) {
            ordemServico.setDataSaida(LocalDateTime.now());
        } else {
            ordemServico.setDataSaida(null);
        }
        ordemServico.setStatus(novoStatus);
        ordemServicoRepository.save(ordemServico);
        }
    }

