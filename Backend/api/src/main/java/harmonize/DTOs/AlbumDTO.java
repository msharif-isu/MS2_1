package harmonize.DTOs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import harmonize.Entities.Album;
import lombok.Data;
import lombok.NonNull;

@Data
public class AlbumDTO {
    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String imageUrl;

    public AlbumDTO(Album album) {
        this.id = album.getId();
        this.name = album.getName();
        this.imageUrl = album.getImageUrl();
    }

    @JsonCreator
    public AlbumDTO(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("imageUrl") String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        AlbumDTO artist = (AlbumDTO) o;
        return artist.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }  
}
