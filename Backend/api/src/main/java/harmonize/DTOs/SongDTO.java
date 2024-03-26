package harmonize.DTOs;

import java.util.Date;

import harmonize.Entities.Song;
import harmonize.Entities.UserSong;
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
    private String artistid;

    @NonNull
    private String title;

    @NonNull
    private String artist;

    private Date time;

    public SongDTO(Song song) {
        this.id = song.getId();
        this.artistid = song.getArtistId();
        this.title = song.getTitle();
        this.artist = song.getArtist();
    }

    public SongDTO(UserSong connection) {
        this.id = connection.getSong().getId();
        this.artistid = connection.getSong().getArtistId();
        this.title = connection.getSong().getTitle();
        this.artist = connection.getSong().getArtist();
        this.time = connection.getTime();
    }
}
