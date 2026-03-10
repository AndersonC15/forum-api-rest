package AndersonC15.service;

import AndersonC15.dto.TemaDTO;
import AndersonC15.entity.Tema;
import AndersonC15.entity.Usuario;
import AndersonC15.repository.TemaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TemaService {

    private final TemaRepository temaRepository;
    private final UsuarioService usuarioService;

    /**
     * CREATE: Crea un nuevo tema
     */
    public TemaDTO crearTema(TemaDTO temaDTO) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Tema tema = Tema.builder()
                .titulo(temaDTO.getTitulo())
                .descripcion(temaDTO.getDescripcion())
                .usuario(usuario)
                .activo(true)
                .build();

        tema = temaRepository.save(tema);
        log.info("Tema creado: {} por usuario: {}", tema.getId(), usuario.getUsername());

        return convertirADTO(tema);
    }

    /**
     * READ: Obtiene todos los temas del usuario autenticado
     */
    @Transactional(readOnly = true)
    public List<TemaDTO> obtenerMisTemas() {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        return temaRepository.findByUsuarioAndActivoTrue(usuario)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * READ: Obtiene un tema específico por ID
     */
    @Transactional(readOnly = true)
    public TemaDTO obtenerTemaPorId(Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Tema tema = temaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado: " + id));

        // Verificar que el tema pertenezca al usuario autenticado
        if (!tema.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para acceder a este tema");
        }

        return convertirADTO(tema);
    }

    /**
     * UPDATE: Actualiza un tema existente
     */
    public TemaDTO actualizarTema(Long id, TemaDTO temaDTO) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Tema tema = temaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado: " + id));

        // Verificar propiedad
        if (!tema.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para actualizar este tema");
        }

        // Actualizar campos
        tema.setTitulo(temaDTO.getTitulo());
        tema.setDescripcion(temaDTO.getDescripcion());
        tema.setFechaActualizacion(System.currentTimeMillis());

        tema = temaRepository.save(tema);
        log.info("Tema actualizado: {} por usuario: {}", tema.getId(), usuario.getUsername());

        return convertirADTO(tema);
    }

    /**
     * DELETE: Elimina lógicamente un tema (soft delete)
     */
    public void eliminarTema(Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        Tema tema = temaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado: " + id));

        // Verificar propiedad
        if (!tema.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para eliminar este tema");
        }

        // Soft delete
        tema.setActivo(false);
        tema.setFechaActualizacion(System.currentTimeMillis());
        temaRepository.save(tema);

        log.info("Tema eliminado: {} por usuario: {}", id, usuario.getUsername());
    }

    /**
     * Convierte una entidad Tema a DTO
     */
    private TemaDTO convertirADTO(Tema tema) {
        return TemaDTO.builder()
                .id(tema.getId())
                .titulo(tema.getTitulo())
                .descripcion(tema.getDescripcion())
                .usuarioId(tema.getUsuario().getId())
                .usernameUsuario(tema.getUsuario().getUsername())
                .fechaCreacion(tema.getFechaCreacion())
                .fechaActualizacion(tema.getFechaActualizacion())
                .activo(tema.getActivo())
                .build();
    }
}