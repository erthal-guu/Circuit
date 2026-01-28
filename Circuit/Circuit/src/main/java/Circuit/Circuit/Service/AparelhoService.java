package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Aparelho;
import Circuit.Circuit.Repository.AparelhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AparelhoService {
    @Autowired
    private AparelhoRepository aparelhoRepository;

    public Aparelho cadastrar(Aparelho aparelho){
        return aparelhoRepository.save(aparelho);
    }
    public Aparelho excluir(Long id){
        Aparelho aparelho = aparelhoRepository.getReferenceById(id);
        aparelho.setAtivo(false);
        return aparelhoRepository.save(aparelho);
    }

    public Aparelho restaurar(Long id){
        Aparelho aparelho = aparelhoRepository.getReferenceById(id);
        aparelho.setAtivo(true);
        return aparelhoRepository.save(aparelho);
    }
    public List<Aparelho> listarAparelhosAtivos(){
        return aparelhoRepository.findByAtivoTrueOrderById();
    }
    public List<Aparelho> listarAparelhosInativos(){
        return aparelhoRepository.findByAtivoFalseOrderById();
    }
    public Aparelho editar(Long id, Aparelho dadosAtualizados){
        Aparelho aparelho = aparelhoRepository.getReferenceById(id);
        aparelho.setModelo(dadosAtualizados.getModelo());
        aparelho.setCliente(dadosAtualizados.getCliente());
        aparelho.setNumeroSerie(dadosAtualizados.getNumeroSerie());
        aparelho.setObservacoes(dadosAtualizados.getObservacoes());
        aparelho.setAtivo(dadosAtualizados.getAtivo());
        return aparelhoRepository.save(aparelho);
    }
}
