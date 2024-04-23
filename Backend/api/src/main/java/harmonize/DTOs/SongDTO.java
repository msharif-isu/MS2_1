package harmonize.DTOs;

import harmonize.Entities.Song;
import harmonize.Entities.LikedSong;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SongDTO {
    @NonNull
    private String id;

    @NonNull
    private String title;

    private ArtistDTO artist;

    public SongDTO(Song song) {
        this.id = song.getId();
        this.title = song.getTitle();
        this.artist = new ArtistDTO(song.getArtist().getId(), song.getArtist().getName());
    }

    public SongDTO(LikedSong connection) {
        this.id = connection.getSong().getId();
        this.title = connection.getSong().getTitle();
        this.artist = new ArtistDTO(connection.getSong().getArtist().getId(), connection.getSong().getArtist().getName());
    }
}
