package harmonize.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class SearchDTO {
    @NonNull
    private String q;
    private String type;
    private String limit;
    private String offset;

    public SearchDTO(String q, String limit, String offset) {
        this.q = q;
        this.offset = offset;
        this.limit = limit;
    }
}
