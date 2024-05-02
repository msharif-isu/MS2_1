package harmonize.DTOs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TransmissionDTO {
    Class<? extends Object> type;
    Object data;

    @JsonCreator
    public TransmissionDTO(@JsonProperty("type") Class<? extends Object> type, @JsonProperty("data") Object data) {
        this.type = type;
        this.data = data;
    }
}
