package Circuit.Circuit.Service;

import Circuit.Circuit.Model.OrdemServico;
import Circuit.Circuit.Repository.OrdemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
