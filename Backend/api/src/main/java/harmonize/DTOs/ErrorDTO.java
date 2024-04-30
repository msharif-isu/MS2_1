package harmonize.DTOs;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ErrorDTO {
    private HttpStatus status;
    private String message;

    @JsonCreator
    public ErrorDTO(@JsonProperty("status") HttpStatus status, @JsonProperty("message") String message) {
        this.status = status;
        this.message = message;
    }
}
