package harmonize.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransmissionDTO {
    Class<? extends Object> type;
    Object data;
}
