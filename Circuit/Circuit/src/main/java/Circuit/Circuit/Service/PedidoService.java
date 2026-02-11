package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Pedido;
import Circuit.Circuit.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    public List<Pedido> listarPedidos(){
        return pedidoRepository.findAll();
    }
}
