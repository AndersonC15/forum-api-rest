package AndersonC15.controller;


import AndersonC15.dto.AuthRequest;
import AndersonC15.dto.AuthResponse;
import AndersonC15.dto.MessageResponse;
import AndersonC15.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UsuarioService usuarioService;

    /**
     * POST /api/auth/registro
     * Registra un nuevo usuario
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody AuthRequest request) {
        try {
            usuarioService.registrarUsuario(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new MessageResponse(
                            HttpStatus.CREATED.value(),
                            "Usuario registrado exitosamente",
                            null
                    ));
        } catch (Exception e) {
            log.error("Error en registro: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage()
                    ));
        }
    }

    /**
     * POST /api/auth/login
     * Autentica un usuario y retorna el token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            AuthResponse authResponse = usuarioService.autenticar(request);

            return ResponseEntity
                    .ok(new MessageResponse(
                            HttpStatus.OK.value(),
                            "Autenticación exitosa",
                            authResponse
                    ));
        } catch (Exception e) {
            log.error("Error en login: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse(
                            HttpStatus.UNAUTHORIZED.value(),
                            "Credenciales inválidas"
                    ));
        }
    }
}