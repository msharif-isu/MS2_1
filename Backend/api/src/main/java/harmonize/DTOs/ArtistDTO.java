package harmonize.DTOs;


import harmonize.Entities.Artist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ArtistDTO {
    @NonNull
    private String id;

    @NonNull
    private String name;

    public ArtistDTO(Artist artist) {
        this.id = artist.getId();
        this.name = artist.getName();
    }
}
