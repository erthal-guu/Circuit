package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Aparelho;
import Circuit.Circuit.Repository.AparelhoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AparelhoService {

    private static final Logger logger = LoggerFactory.getLogger(AparelhoService.class);

    @Autowired
    private AparelhoRepository aparelhoRepository;

    public Aparelho cadastrar(Aparelho aparelho){
        logger.info("Tentativa de cadastro de novo aparelho - Modelo: {}, Número de Série: {}, Cliente: {}",
                aparelho.getModelo(), aparelho.getNumeroSerie(), aparelho.getCliente());
        return aparelhoRepository.save(aparelho);
    }
    public Aparelho excluir(Long id){
        Aparelho aparelho = aparelhoRepository.getReferenceById(id);
        logger.info("Tentativa de exclusão de aparelho - ID: {}, Modelo: {}, Número de Série: {}, Cliente: {}",
                aparelho.getId(), aparelho.getModelo(), aparelho.getNumeroSerie(), aparelho.getCliente());
        aparelho.setAtivo(false);
        Aparelho aparelhoSalvo = aparelhoRepository.save(aparelho);
        logger.info("Aparelho desativado com sucesso - ID: {}, Modelo: {}, Número de Série: {}, Cliente: {}",
                aparelhoSalvo.getId(), aparelhoSalvo.getModelo(), aparelhoSalvo.getNumeroSerie(), aparelhoSalvo.getCliente());
        return aparelhoSalvo;
    }
    public Aparelho restaurar(Long id){
        Aparelho aparelho = aparelhoRepository.getReferenceById(id);
        logger.info("Tentativa de restauração de aparelho - ID: {}, Modelo: {}, Número de Série: {}, Cliente: {}",
                aparelho.getId(), aparelho.getModelo(), aparelho.getNumeroSerie(), aparelho.getCliente());
        aparelho.setAtivo(true);
        Aparelho aparelhoSalvo = aparelhoRepository.save(aparelho);
        logger.info("Aparelho restaurado com sucesso - ID: {}, Modelo: {}, Número de Série: {}, Cliente: {}",
                aparelhoSalvo.getId(), aparelhoSalvo.getModelo(), aparelhoSalvo.getNumeroSerie(), aparelhoSalvo.getCliente());
        return aparelhoSalvo;
    }
    public List<Aparelho> listarAparelhosAtivos(){
        List<Aparelho> aparelhos = aparelhoRepository.findByAtivoTrueOrderById();
        logger.debug("Listagem de aparelhos ativos - {} registros encontrados", aparelhos.size());
        return aparelhos;
    }
    public List<Aparelho> listarAparelhosInativos(){
        List<Aparelho> aparelhos = aparelhoRepository.findByAtivoFalseOrderById();
        logger.debug("Listagem de aparelhos inativos - {} registros encontrados", aparelhos.size());
        return aparelhos;
    }
    public Aparelho editar(Long id, Aparelho dadosAtualizados){
        Aparelho aparelho = aparelhoRepository.getReferenceById(id);
        logger.info("Tentativa de edição de aparelho - ID: {}, Modelo atual: {}, Modelo novo: {}, Cliente: {}",
                id, aparelho.getModelo(), dadosAtualizados.getModelo(), aparelho.getCliente());
        aparelho.setModelo(dadosAtualizados.getModelo());
        aparelho.setCliente(dadosAtualizados.getCliente());
        aparelho.setNumeroSerie(dadosAtualizados.getNumeroSerie());
        aparelho.setObservacoes(dadosAtualizados.getObservacoes());
        aparelho.setAtivo(dadosAtualizados.getAtivo());
        Aparelho aparelhoSalvo = aparelhoRepository.save(aparelho);
        logger.info("Aparelho editado com sucesso - ID: {}, Modelo: {}, Número de Série: {}, Cliente: {}",
                aparelhoSalvo.getId(), aparelhoSalvo.getModelo(), aparelhoSalvo.getNumeroSerie(), aparelhoSalvo.getCliente());
        return aparelhoSalvo;
    }
    public List<Aparelho> buscarAtivosPorCliente(Long clienteId) {
        List<Aparelho> aparelhos = aparelhoRepository.findByClienteIdAndAtivoTrue(clienteId);
        logger.debug("Busca de aparelhos ativos por cliente ID: {} - {} aparelhos encontrados", clienteId, aparelhos.size());
        return aparelhos;
    }
}
