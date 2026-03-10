package AndersonC15.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemaDTO {

    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    private String descripcion;

    private Long usuarioId;
    private String usernameUsuario;
    private Long fechaCreacion;
    private Long fechaActualizacion;
    private Boolean activo;
}