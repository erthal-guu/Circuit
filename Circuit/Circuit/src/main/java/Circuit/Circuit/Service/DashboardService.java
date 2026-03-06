package Circuit.Circuit.Service;

import Circuit.Circuit.Repository.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    @Autowired
    private DashboardRepository dashboardRepository;

    public Double receitaTotal() {
        return dashboardRepository.calcularReceitaTotal();
    }
    public Double despesaTotal() {
        return dashboardRepository.calcularDespesaTotal();
    }
    public Double saldoLiquido(){
        Double receitaTotal = dashboardRepository.calcularReceitaTotal();
        Double despesaTotal = dashboardRepository.calcularDespesaTotal();

        return  receitaTotal - despesaTotal;
    }
}
