package school.sptech.prova_ac1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    // listar todos os usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {
        List<Usuario> usuarios = repository.findAll();
        if (usuarios.isEmpty()){
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(usuarios); // 200
//        return ResponseEntity.internalServerError().build();
    }

    // criar/cadastrar novo usuario
    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        if(repository.findByEmail(usuario.getEmail()).isPresent() ||
            repository.findByCpf(usuario.getCpf()).isPresent()){
            return ResponseEntity.status(409).build(); // 409 Conflito
        }
        Usuario novo = repository.save(usuario);
        return ResponseEntity.status(201).body(novo); // 201 Criado
//        return ResponseEntity.internalServerError().build();
    }

     // buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        Usuario usuario = repository.findById(id).orElse(null);
        if (usuario == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);

        //        return ResponseEntity.internalServerError().build();
    }

    //deletar usuario por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (repository.findById(id).isEmpty()){
            return ResponseEntity.notFound().build(); // 404
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
        //        return ResponseEntity.internalServerError().build();
    }

    // buscando pela data de nascimento
    @GetMapping("/filtro-data")
public ResponseEntity<List<Usuario>> buscarPorDataNascimento(@RequestParam LocalDate nascimento) {
        List<Usuario> usuarios = repository.findByDataNascimentoGreaterThan(nascimento);

        if (usuarios.isEmpty()){
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(usuarios); // 200
        //        return ResponseEntity.internalServerError().build();
    }

    // atualizando usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable Integer id,
            @RequestBody Usuario usuario
    ) {
        Optional<Usuario> existente = repository.findById(id);
        if(existente.isEmpty()){
            return ResponseEntity.notFound().build(); // 404
        }

        //email duplicado
        Optional<Usuario> emailExistente = repository.findByEmail(usuario.getEmail());
        if (emailExistente.isPresent() && !emailExistente.get().getId().equals(id)){
            return ResponseEntity.status(409).build(); // 409 conflito
        }

        //cpf duplicado
        Optional<Usuario> cpfExistente = repository.findByCpf(usuario.getCpf());
        if (cpfExistente.isPresent() && !cpfExistente.get().getId().equals(id)){
            return ResponseEntity.status(409).build();
        }

        usuario.setId(id);  //atualizando o correto
        Usuario atualizado = repository.save(usuario);
        return  ResponseEntity.ok(atualizado); //200

        //        return ResponseEntity.internalServerError().build();
    }
}
