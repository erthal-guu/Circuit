package Circuit.Circuit.Controller;

import Circuit.Circuit.Service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/{tipo}")
    public ResponseEntity<List<Map<String, Object>>> gerarRelatorio(
            @PathVariable String tipo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        List<Map<String, Object>> dados;

        switch (tipo.toLowerCase()) {
            case "vendas":
                dados = relatorioService.getRelatorioVendas(inicio, fim);
                break;
            case "estoque":
                dados = relatorioService.getRelatorioEstoque(inicio, fim);
                break;
            case "financeiro":
                dados = relatorioService.getRelatorioFinanceiro(inicio, fim);
                break;
            default:
                return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(dados);
    }
}