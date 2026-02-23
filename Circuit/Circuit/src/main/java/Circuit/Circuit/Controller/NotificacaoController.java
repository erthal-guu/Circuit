package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.Notificacao;
import Circuit.Circuit.Service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    @GetMapping("/pendentes")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> listarPendentes(@RequestParam String tipo) {
        List<Notificacao> notificacoes = notificacaoService.listarPendentes(tipo);

        List<Map<String, Object>> response = notificacoes.stream().map(n -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", n.getId());
            map.put("mensagem", n.getMensagem());
            map.put("codigo", n.getPedido().getCodigo());
            map.put("tempoAtras", calcularTempo(n.getDataCriacao()));
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/efetivar/{id}")
    @ResponseBody
    public ResponseEntity<?> efetivar(@PathVariable Long id) {
        try {
            notificacaoService.efetivarEntrada(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String calcularTempo(LocalDateTime dataCriacao) {
        Duration duration = Duration.between(dataCriacao, LocalDateTime.now());
        if (duration.toMinutes() < 60) return "Há " + duration.toMinutes() + " min";
        if (duration.toHours() < 24) return "Há " + duration.toHours() + " h";
        return "Há " + duration.toDays() + " dias";
    }
}