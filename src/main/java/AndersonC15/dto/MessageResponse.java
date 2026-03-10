package AndersonC15.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {

    private int status;
    private String message;
    private Object data;
    private Long timestamp;

    public MessageResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public MessageResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
}