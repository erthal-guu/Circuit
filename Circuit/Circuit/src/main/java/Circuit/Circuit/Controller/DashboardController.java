package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.ContasPagar;
import Circuit.Circuit.Model.ContasReceber;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import Circuit.Circuit.Service.ContaReceberService;
import Circuit.Circuit.Service.ContasPagarService;
import Circuit.Circuit.Service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/financeiro")
public class DashboardController {
    @Autowired
    private ContaReceberService contaReceberService;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public String abrirDashboards(Model model) {
        Double receitaTotal = dashboardService.receitaTotal();
        Double despesaTotal = dashboardService.despesaTotal();
        Double saldoLiquido = dashboardService.saldoLiquido();

        model.addAttribute("receitaTotal", receitaTotal);
        model.addAttribute("saldoLiquido", saldoLiquido);
        model.addAttribute("despesaTotal", despesaTotal);
        return "financeiro";
    }
}
