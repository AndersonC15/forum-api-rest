package AndersonC15.service;

import AndersonC15.dto.AuthRequest;
import AndersonC15.dto.AuthResponse;
import AndersonC15.entity.Usuario;
import AndersonC15.repository.UsuarioRepository;
import AndersonC15.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    /**
     * Registra un nuevo usuario
     */
    public Usuario registrarUsuario(AuthRequest request) {
        // Validar que el usuario no exista
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El username ya existe: " + request.getUsername());
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya existe: " + request.getEmail());
        }

        // Crear nuevo usuario
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .activo(true)
                .build();

        usuario = usuarioRepository.save(usuario);
        log.info("Usuario registrado exitosamente: {}", usuario.getUsername());

        return usuario;
    }

    /**
     * Autentica un usuario y retorna el token JWT
     */
    public AuthResponse autenticar(AuthRequest request) {
        // Autenticar las credenciales
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Validar que el usuario esté activo
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        // Establecer autenticación en el contexto
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generar token JWT
        String token = tokenProvider.generateToken(authentication);
        long expiresIn = tokenProvider.getExpirationTimeInMillis(token);

        log.info("Usuario autenticado: {}", usuario.getUsername());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .expiresIn(expiresIn)
                .build();
    }

    /**
     * Obtiene el usuario autenticado actualmente
     */
    @Transactional(readOnly = true)
    public Usuario obtenerUsuarioAutenticado() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}