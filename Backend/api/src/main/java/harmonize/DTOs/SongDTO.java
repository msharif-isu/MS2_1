package harmonize.DTOs;

import harmonize.Entities.Song;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import harmonize.Entities.LikedSong;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SongDTO {
    @NonNull
    private String id;

    @NonNull
    private String artistid;

    @NonNull
    private String title;

    //For Testing
    @Deprecated
    private String artist;

    public SongDTO(Song song) {
        this.id = song.getId();
        this.artistid = song.getArtistId();
        this.title = song.getTitle();
        this.artist = song.getArtist();
    }

    public SongDTO(LikedSong connection) {
        this.id = connection.getSong().getId();
        this.artistid = connection.getSong().getArtistId();
        this.title = connection.getSong().getTitle();
        this.artist = connection.getSong().getArtist();
    }

    @JsonCreator
    public SongDTO(@JsonProperty("id") String id, @JsonProperty("artistid") String artistid, @JsonProperty("title") String title, @JsonProperty("artist") String artist) {
        this.id = id;
        this.artistid = artistid;
        this.title = title;
        this.artist = artist;
    }
}
