package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Modelo;
import Circuit.Circuit.Repository.MarcaRepository;
import Circuit.Circuit.Repository.ModeloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModeloService {

    private static final Logger logger = LoggerFactory.getLogger(ModeloService.class);

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    public List<Modelo> listarModelosAtivos(){
        List<Modelo> modelos = modeloRepository.findByAtivoTrueOrderById();
        logger.debug("Listagem de modelos ativos - {} registros encontrados", modelos.size());
        return modelos;
    }

    public List<Modelo> listarModelosInativos(){
        List<Modelo> modelos = modeloRepository.findByAtivoFalseOrderById();
        logger.debug("Listagem de modelos inativos - {} registros encontrados", modelos.size());
        return modelos;
    }

    public Modelo cadastrar(Modelo modelo){
        logger.info("Tentativa de cadastro de novo modelo - Nome: {}, Marca: {}",
                modelo.getNome(), modelo.getMarca() != null ? modelo.getMarca().getNome() : "N/A");
        return modeloRepository.save(modelo);
    }

    public Modelo excluir(Long id){
        Modelo modelo = modeloRepository.getReferenceById(id);
        logger.info("Tentativa de exclusão de modelo - ID: {}, Nome: {}, Marca: {}",
                modelo.getId(), modelo.getNome(), modelo.getMarca() != null ? modelo.getMarca().getNome() : "N/A");
        modelo.setAtivo(false);
        Modelo modeloSalvo = modeloRepository.save(modelo);
        logger.info("Modelo desativado com sucesso - ID: {}, Nome: {}, Marca: {}",
                modeloSalvo.getId(), modeloSalvo.getNome(), modeloSalvo.getMarca() != null ? modeloSalvo.getMarca().getNome() : "N/A");
        return modeloSalvo;
    }

    public Modelo restaurar(Long id){
        Modelo modelo = modeloRepository.getReferenceById(id);
        logger.info("Tentativa de restauração de modelo - ID: {}, Nome: {}, Marca: {}",
                modelo.getId(), modelo.getNome(), modelo.getMarca() != null ? modelo.getMarca().getNome() : "N/A");
        modelo.setAtivo(true);
        Modelo modeloSalvo = modeloRepository.save(modelo);
        logger.info("Modelo restaurado com sucesso - ID: {}, Nome: {}, Marca: {}",
                modeloSalvo.getId(), modeloSalvo.getNome(), modeloSalvo.getMarca() != null ? modeloSalvo.getMarca().getNome() : "N/A");
        return modeloSalvo;
    }

    public Modelo editar(Long id, Modelo dadosAtualizados){
        Modelo modelo = modeloRepository.getReferenceById(id);
        logger.info("Tentativa de edição de modelo - ID: {}, Nome atual: {}, Nome novo: {}, Marca: {}",
                id, modelo.getNome(), dadosAtualizados.getNome(), modelo.getMarca() != null ? modelo.getMarca().getNome() : "N/A");
        modelo.setNome(dadosAtualizados.getNome());
        modelo.setMarca(dadosAtualizados.getMarca());
        modelo.setAtivo(dadosAtualizados.getAtivo());
        Modelo modeloSalvo = modeloRepository.save(modelo);
        logger.info("Modelo editado com sucesso - ID: {}, Nome: {}, Marca: {}",
                modeloSalvo.getId(), modeloSalvo.getNome(), modeloSalvo.getMarca() != null ? modeloSalvo.getMarca().getNome() : "N/A");
        return modeloSalvo;
    }
}
