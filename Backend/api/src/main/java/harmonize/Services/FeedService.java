package harmonize.Services;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import harmonize.DTOs.RecommendationDTO;
import harmonize.DTOs.SearchDTO;
import harmonize.Entities.FeedItem;
import harmonize.Entities.Song;
import harmonize.Entities.SongFeedItem;
import harmonize.Entities.User;
import harmonize.Repositories.FeedRepository;
import harmonize.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.websocket.Session;

@Service
public class FeedService {
    private static Set<Session> sessions = new HashSet<>();

    private MusicService musicService;
    private UserRepository userRepository;
    private FeedRepository feedRepository;
    private ObjectMapper mapper;

    @Autowired
    public FeedService(MusicService musicService, UserRepository userRepository, FeedRepository feedRepository, ObjectMapper mapper) {
        this.musicService = musicService;
        this.userRepository = userRepository;
        this.feedRepository = feedRepository;
        this.mapper = mapper;
    }

    public void onOpen(Session session) throws IOException {
        loadFeed(session);
        List<FeedItem> feed = ((List<?>)session.getUserProperties().get("feed")).stream().map(FeedItem.class::cast).collect(Collectors.toList());

        System.out.println(session.getUserPrincipal().getName());

        for(int i = 0; i < feed.size(); i++) {
            System.out.println(((SongFeedItem)feed.get(i)).getSong().getTitle());
        }

        sessions.add(session);
    }

    public void onMessage(Session session, String message) throws IOException {
        loadFeed(session);
        List<FeedItem> feed = ((List<?>)session.getUserProperties().get("feed")).stream().map(FeedItem.class::cast).collect(Collectors.toList());

        for(int i = 0; i < 5; i++) {
            session.getBasicRemote().sendText(mapper.writeValueAsString(feed.get(i)));
            feed.remove(i);
        }

        session.getUserProperties().put("feed", feed);
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

    private List<Song> getRecommendedSongs(User user) {
        List<Song> songRec = new ArrayList<>();
        List<String> artistIds = new ArrayList<>();
        List<String> songIds = new ArrayList<>();

        for(int i = 0; i < user.getTopArtists().size() && i < 3; i++)
            artistIds.add(user.getTopArtists().get(i));

        for(int i = 0; i < user.getLikedSongs().size() && i < 2; i++)
            songIds.add(user.getLikedSongs().get(i).getSong().getId());

        JsonNode recommendations = musicService.getRecommendations(new RecommendationDTO(Integer.toString(100), artistIds, songIds));
        
        for(int i = 0; i < recommendations.get("tracks").size(); i++)
            songRec.add(new Song(recommendations.get("tracks").get(i)));

        return songRec;
    }

    private void loadFeed(Session session) {
        List<FeedItem> feed;
        User user = userRepository.findByUsername(session.getUserPrincipal().getName());

        feed = session.getUserProperties().containsKey("feed") ? 
                    ((List<?>)session.getUserProperties().get("feed")).stream().map(FeedItem.class::cast).collect(Collectors.toList()) : 
                    generateFeed(user);

        session.getUserProperties().put("user", user);
        session.getUserProperties().put("feed", feed);
    }

    @Transactional
    private List<FeedItem> generateFeed(User user) {
        List<FeedItem> feed = new ArrayList<>();

        for(String artistId : user.getTopArtists()) {
            for(Song song : getNewReleases(artistId)) {
                feed.add(new SongFeedItem("new_release", song));
            }
        }

        for(Song song : getRecommendedSongs(user)) {
            feed.add(new SongFeedItem("recommendation", song));
        }

        Collections.shuffle(feed);

        return feed;
    }
}