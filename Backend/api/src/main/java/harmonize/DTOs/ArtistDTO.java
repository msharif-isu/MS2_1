package harmonize.DTOs;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import harmonize.Entities.Artist;
import lombok.Data;
import lombok.NonNull;

@Data
public class ArtistDTO {
    @NonNull
    private String id;

    @NonNull
    private String name;

    public ArtistDTO(Artist artist) {
        this.id = artist.getId();
        this.name = artist.getName();
    }

    @JsonCreator
    public ArtistDTO(@JsonProperty("id") String id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
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
