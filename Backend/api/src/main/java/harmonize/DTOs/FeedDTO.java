package harmonize.DTOs;

import harmonize.Enum.FeedEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedDTO {
    private FeedEnum requestType;
    private Object data;
}
