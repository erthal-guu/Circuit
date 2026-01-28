package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Modelo;
import Circuit.Circuit.Model.Peca;
import Circuit.Circuit.Repository.MarcaRepository;
import Circuit.Circuit.Repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class ModeloService {
    @Autowired
    private ModeloRepository modeloRepository;

    public List<Modelo> ListarModelosAtivos(){
        return modeloRepository.findByAtivoTrueOrderById();
    }
    public List<Modelo> ListarModelosInativos(){
        return modeloRepository.findByAtivoFalseOrderById();
    }
    public Modelo cadastrar(Modelo modelo){
        return modeloRepository.save(modelo);
    }

    public Modelo excluir(Long id){
       Modelo modelo =  modeloRepository.getReferenceById(id);
        modelo.setAtivo(false);
        return modeloRepository.save(modelo);
    }

    public Modelo restaurar(Long id){
        Modelo modelo =  modeloRepository.getReferenceById(id);
        modelo.setAtivo(true);
        return modeloRepository.save(modelo);
    }
    public Modelo editar(Long id,Modelo dadosAtualizados){
        Modelo modelo = modeloRepository.getReferenceById(id);
        modelo.setNome(dadosAtualizados.getNome());
        modelo.setMarca( dadosAtualizados.getMarca());
        modelo.setAtivo(dadosAtualizados.getAtivo());
        return modeloRepository.save(modelo);
    }
}
