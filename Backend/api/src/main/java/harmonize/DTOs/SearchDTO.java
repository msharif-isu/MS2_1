package harmonize.DTOs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class SearchDTO {
    @NonNull
    private String q;
    private String type;
    private String limit;
    private String offset;

    @JsonCreator
    public SearchDTO(@JsonProperty("q") String q, @JsonProperty("type") String type, @JsonProperty("limit") String limit, @JsonProperty("offset") String offset) {
        this.q = q;
        this.type = type;
        this.limit = limit;
        this.offset = offset;
    }
}
