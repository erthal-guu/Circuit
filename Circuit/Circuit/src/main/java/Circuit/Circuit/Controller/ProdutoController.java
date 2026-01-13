package Circuit.Circuit.Controller;

import Circuit.Circuit.Model.Produto;
import Circuit.Circuit.Service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/estoque")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping(value = "/cadastrar", consumes = {"multipart/form-data"})
    public ResponseEntity<Produto> cadastrar(
            @RequestPart("produto") Produto produto,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem) {

        return ResponseEntity.ok(produtoService.cadastrar(produto));
    }

    @GetMapping("/listar")
    public List<Produto> listar() {
        return produtoService.listarProdutos();
    }

    @GetMapping("/listar-inativos")
    public List<Produto> listarInativos() {
        return produtoService.listarProdutosInativos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.pesquisarAtivos(null).stream()
                .filter(p -> p.getId().equals(id)).findFirst().orElse(null));
    }

    @PutMapping(value = "/editar/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Produto> editar(
            @PathVariable Long id,
            @RequestPart("produto") Produto produto,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem) {

        return ResponseEntity.ok(produtoService.editarProduto(id, produto));
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluirProduto(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/restaurar/{id}")
    public ResponseEntity<Void> restaurar(@PathVariable Long id) {
        produtoService.restaurarProduto(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pesquisar")
    public List<Produto> pesquisar(@RequestParam(required = false) String nome) {
        return produtoService.pesquisarAtivos(nome);
    }
}