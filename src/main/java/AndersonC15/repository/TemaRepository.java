package AndersonC15.repository;

import AndersonC15.entity.Tema;
import AndersonC15.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemaRepository extends JpaRepository<Tema, Long> {

    List<Tema> findByUsuarioAndActivoTrue(Usuario usuario);

    List<Tema> findByUsuario(Usuario usuario);

    boolean existsByIdAndUsuario(Long id, Usuario usuario);
}