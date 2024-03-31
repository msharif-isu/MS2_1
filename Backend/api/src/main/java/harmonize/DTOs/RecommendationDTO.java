package harmonize.DTOs;

import java.util.List;

import harmonize.ErrorHandling.Exceptions.InternalServerErrorException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class RecommendationDTO {
    @NonNull
    private String limit;
    private String artistIds;
    private String songIds;

    public RecommendationDTO(String limit, List<String> artistIds, List<String> songIds) {
        if(artistIds.size() + songIds.size() > 5)
            throw new InternalServerErrorException("Combined size of artistIds and songIds must be equal to or less than 5");

        this.limit = limit;
        this.artistIds = String.join(",", artistIds);
        this.songIds = String.join(",", songIds);
    }
}
