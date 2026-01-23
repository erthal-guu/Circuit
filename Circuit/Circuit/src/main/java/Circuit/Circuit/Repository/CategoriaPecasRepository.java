package Circuit.Circuit.Repository;

import Circuit.Circuit.Model.Categoria;
import Circuit.Circuit.Model.CategoriaPecas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaPecasRepository extends JpaRepository<CategoriaPecas,Long> {
    List<CategoriaPecas> findAllByOrderByTipoAsc();
}
