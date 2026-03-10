package AndersonC15.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "temas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private Long fechaCreacion = System.currentTimeMillis();

    @Column(name = "fecha_actualizacion")
    private Long fechaActualizacion = System.currentTimeMillis();

    @Column(nullable = false)
    private Boolean activo = true;
}