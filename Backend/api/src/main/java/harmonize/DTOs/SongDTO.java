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
    private String title;

    private ArtistDTO artist;

    private AlbumDTO album;

    public SongDTO(Song song) {
        this.id = song.getId();
        this.title = song.getTitle();
        this.artist = new ArtistDTO(song.getArtist().getId(), song.getArtist().getName());
        this.album = new AlbumDTO(song.getAlbum());
    }

    public SongDTO(LikedSong connection) {
        this.id = connection.getSong().getId();
        this.title = connection.getSong().getTitle();
        this.artist = new ArtistDTO(connection.getSong().getArtist().getId(), connection.getSong().getArtist().getName());
        this.album = new AlbumDTO(connection.getSong().getAlbum());
    }

    @JsonCreator
    public SongDTO(@JsonProperty("id") String id, @JsonProperty("title") String title, @JsonProperty("artist") ArtistDTO artist, @JsonProperty("album") AlbumDTO album) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
    }
}
