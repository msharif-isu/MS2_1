package harmonize.DTOs;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ResponseDTO {
    private HttpStatus status;
    private String message;

    public ResponseDTO(String message) {
        this.status = HttpStatus.OK;
        this.message = message;
    }

    @JsonCreator
    public ResponseDTO(@JsonProperty("status") HttpStatus status, @JsonProperty("message") String message) {
        this.status = status;
        this.message = message;
    }
}
