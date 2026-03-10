package AndersonC15.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private Long expiresIn;  // En milisegundos
}