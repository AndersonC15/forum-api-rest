package AndersonC15.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private Long fechaCreacion = System.currentTimeMillis();
}