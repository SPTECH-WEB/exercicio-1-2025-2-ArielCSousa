package school.sptech.prova_ac1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCpf(String cpf);

    List<Usuario> findByDataNascimentoGreaterThan(LocalDate nascimento);
}
