package harmonize.Services;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import harmonize.DTOs.RecommendationDTO;
import harmonize.DTOs.SearchDTO;
import harmonize.Entities.Song;
import harmonize.Entities.User;
import jakarta.websocket.Session;

@Service
public class FeedService {
    private MusicService musicService;

    @Autowired
    public FeedService(MusicService musicService) {
        this.musicService = musicService;
    }

    public void onOpen(Session session) throws IOException {

    }

    public void onMessage(Session session, String message) throws IOException {

    }

    public void onClose(Session session) throws IOException {

    }

    public void onError(Session session, Throwable throwable) {

    }

    private List<Song> getNewReleases(String artistId) {
        int limit = 50;
        int offset = 0;
        List<Song> newReleases = new ArrayList<>();
        JsonNode albums;

        do {
            SearchDTO search = new SearchDTO(artistId, "album,single", Integer.toString(limit), Integer.toString(offset));
            albums = musicService.getArtistAlbums(search);

            for(int i = 0; i < albums.get("items").size(); i++) {
                String releaseDate = albums.get("items").get(i).get("release_date").asText();
                String releaseYear = releaseDate.substring(0, 4);

                if(Integer.parseInt(releaseYear) != Year.now().getValue())
                    break;
                    
                newReleases.addAll(getAlbumSongs(albums.get("items").get(i).get("id").asText()));
            }

            offset += limit;
        } while(albums.get("items").size() == limit);

        return newReleases;
    }

    private List<Song> getAlbumSongs(String albumId) {
        int limit = 50;
        int offset = 0;
        List<Song> songs = new ArrayList<>();
        JsonNode album;

        do {
            SearchDTO search = new SearchDTO(albumId, Integer.toString(limit), Integer.toString(offset));
            album = musicService.getAlbumSongs(search);

            for(int i = 0; i < album.get("items").size(); i++)
                songs.add(new Song(album.get("items").get(i)));

            offset += limit;
        } while(album.get("items").size() == limit);

        return songs;
    }

    private JsonNode getRecommendedSongs(User user) {
        List<String> artistIds = new ArrayList<>();
        List<String> songIds = new ArrayList<>();

        for(int i = 0; i < 3; i++)
            artistIds.add(user.getTopArtists().get(i));

        for(int i = 0; i < 2; i++)
            songIds.add(user.getLikedSongs().get(i).getSong().getId());
        
        return musicService.getRecommendations(new RecommendationDTO(Integer.toString(100), artistIds, songIds));
    }
}