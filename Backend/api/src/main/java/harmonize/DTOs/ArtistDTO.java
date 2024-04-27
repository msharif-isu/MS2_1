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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ArtistDTO artist = (ArtistDTO) o;
        return artist.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
