package AndersonC15.controller;


import AndersonC15.dto.MessageResponse;
import AndersonC15.dto.TemaDTO;
import AndersonC15.service.TemaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/temas")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('USER')")  // Todos los endpoints requieren autenticación
public class TemaController {

    private final TemaService temaService;

    /**
     * POST /api/temas
     * CREATE: Crea un nuevo tema
     * Requiere: Token JWT válido
     */
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody TemaDTO temaDTO) {
        try {
            TemaDTO temaCreado = temaService.crearTema(temaDTO);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new MessageResponse(
                            HttpStatus.CREATED.value(),
                            "Tema creado exitosamente",
                            temaCreado
                    ));
        } catch (Exception e) {
            log.error("Error al crear tema: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage()
                    ));
        }
    }

    /**
     * GET /api/temas
     * READ: Obtiene todos los temas del usuario autenticado
     * Requiere: Token JWT válido
     */
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<TemaDTO> temas = temaService.obtenerMisTemas();

            return ResponseEntity
                    .ok(new MessageResponse(
                            HttpStatus.OK.value(),
                            "Temas obtenidos exitosamente",
                            temas
                    ));
        } catch (Exception e) {
            log.error("Error al obtener temas: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            e.getMessage()
                    ));
        }
    }

    /**
     * GET /api/temas/{id}
     * READ: Obtiene un tema específico
     * Requiere: Token JWT válido
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            TemaDTO tema = temaService.obtenerTemaPorId(id);

            return ResponseEntity
                    .ok(new MessageResponse(
                            HttpStatus.OK.value(),
                            "Tema obtenido exitosamente",
                            tema
                    ));
        } catch (Exception e) {
            log.error("Error al obtener tema: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(
                            HttpStatus.NOT_FOUND.value(),
                            e.getMessage()
                    ));
        }
    }

    /**
     * PUT /api/temas/{id}
     * UPDATE: Actualiza un tema existente
     * Requiere: Token JWT válido y ser propietario del tema
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @Valid @RequestBody TemaDTO temaDTO) {
        try {
            TemaDTO temaActualizado = temaService.actualizarTema(id, temaDTO);

            return ResponseEntity
                    .ok(new MessageResponse(
                            HttpStatus.OK.value(),
                            "Tema actualizado exitosamente",
                            temaActualizado
                    ));
        } catch (Exception e) {
            log.error("Error al actualizar tema: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage()
                    ));
        }
    }

    /**
     * DELETE /api/temas/{id}
     * DELETE: Elimina un tema
     * Requiere: Token JWT válido y ser propietario del tema
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            temaService.eliminarTema(id);

            return ResponseEntity
                    .ok(new MessageResponse(
                            HttpStatus.OK.value(),
                            "Tema eliminado exitosamente"
                    ));
        } catch (Exception e) {
            log.error("Error al eliminar tema: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage()
                    ));
        }
    }
}