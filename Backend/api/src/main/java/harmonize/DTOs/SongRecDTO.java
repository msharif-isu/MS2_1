package harmonize.DTOs;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import harmonize.ErrorHandling.Exceptions.InternalServerErrorException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class SongRecDTO {
    @NonNull
    private String limit;
    private List<String> artistIds;
    private List<String> songIds;

    @JsonCreator
    public SongRecDTO(@JsonProperty("limit") String limit, @JsonProperty("artistIds") List<String> artistIds, @JsonProperty("songIds") List<String> songIds) {
        if(artistIds.size() + songIds.size() > 5)
            throw new InternalServerErrorException("Combined size of artistIds and songIds must be equal to or less than 5");

        this.limit = limit;
        this.artistIds = artistIds;
        this.songIds = songIds;
    }
}
