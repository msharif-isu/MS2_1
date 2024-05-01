package harmonize.DTOs;

import harmonize.Entities.Album;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
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
