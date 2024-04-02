package harmonize.Services;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import harmonize.DTOs.FeedDTO;
import harmonize.DTOs.RecommendationDTO;
import harmonize.DTOs.SearchDTO;
import harmonize.DTOs.TransmissionDTO;
import harmonize.Entities.Song;
import harmonize.Entities.User;
import harmonize.Entities.FeedItems.AbstractFeedItem;
import harmonize.Entities.FeedItems.SongFeedItem;
import harmonize.Enum.FeedEnum;
import harmonize.ErrorHandling.Exceptions.InternalServerErrorException;
import harmonize.ErrorHandling.Exceptions.UserNotFoundException;
import harmonize.Repositories.UserRepository;
import harmonize.Repositories.FeedRepositories.AbstractFeedRepository;
import harmonize.Repositories.FeedRepositories.SongFeedRepository;
import jakarta.transaction.Transactional;
import jakarta.websocket.Session;

@Service
public class FeedService {
    private static Set<Session> sessions = new HashSet<>();

    private Map<Class<? extends AbstractFeedItem>, AbstractFeedRepository> feedRepositories;

    private MusicService musicService;
    private UserRepository userRepository;
    private ObjectMapper mapper;

    @Autowired
    public FeedService(MusicService musicService, UserRepository userRepository, SongFeedRepository songFeedRepository, ObjectMapper mapper) {
        this.musicService = musicService;
        this.userRepository = userRepository;
        this.mapper = mapper;

        this.feedRepositories = new HashMap<>();
        this.feedRepositories.put(SongFeedItem.class, songFeedRepository);
    }

    public void onOpen(Session session) throws IOException {
        loadFeed(session);
        sessions.add(session);
    }

    public void onMessage(Session session, String message) throws IOException {
        loadFeed(session);
        List<AbstractFeedItem> feed = ((List<?>)session.getUserProperties().get("feed")).stream().map(AbstractFeedItem.class::cast).collect(Collectors.toList());
        User user = (User)session.getUserProperties().get("user");
        
        JsonNode jsonMessage = mapper.readTree(message);
        if (!jsonMessage.at("/type").textValue().equals(FeedDTO.class.getName())) {
            onError(session, new InternalServerErrorException("Could not parse message."), false);
            return;
        }

        if(feed.isEmpty()) {
            send(session, FeedEnum.NO_DATA);
            return;
        }

        if(jsonMessage.at("/body/requestType").asInt() == FeedEnum.NEW_PAGE.ordinal()) {  
            int page = jsonMessage.at("/body/data/page").asInt();
            int limit = jsonMessage.at("/body/data/limit").asInt();

            for(AbstractFeedItem item : feed.subList((page - 1) * limit, page * limit)) {
                if(item instanceof SongFeedItem)
                    musicService.getSong(((SongFeedItem)item).getSong().getId());

                feedRepositories.get(item.getClass()).save(item);
                user.getSeenFeed().add(item);

                send(session, item);
            }
        }

        session.getUserProperties().put("feed", feed);
    }

    public void onClose(Session session) throws IOException {
        loadFeed(session);
        User user = (User)session.getUserProperties().get("user");
        if(user != null)
            userRepository.save(user);
        sessions.remove(session);
    }

    public void onError(Session session, Throwable throwable) {
        onError(session, throwable, true);
    }

    public void onError(Session session, Throwable throwable, Boolean closeSession) {
        throwable.printStackTrace();
        try {
            send(session, throwable);
            if (closeSession)
                session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(Session session, Object obj) throws IOException {
        session.getBasicRemote().sendText(mapper.writeValueAsString(new TransmissionDTO(obj.getClass(), obj)));
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

    private void loadFeed(Session session) throws IOException {
        User user = session.getUserProperties().containsKey("user") ?
                        (User)session.getUserProperties().get("user") :
                        userRepository.findByUsername(session.getUserPrincipal().getName());

        if(user == null) {
            onError(session, new UserNotFoundException(session.getUserPrincipal().getName()), true);
            return;
        }

        List<AbstractFeedItem> feed = session.getUserProperties().containsKey("feed") ? 
                                ((List<?>)session.getUserProperties().get("feed")).stream().map(AbstractFeedItem.class::cast).collect(Collectors.toList()) : 
                                generateFeed(user);

        session.getUserProperties().put("user", user);
        session.getUserProperties().put("feed", feed);
    }

    @Transactional
    private List<AbstractFeedItem> generateFeed(User user) {
        List<AbstractFeedItem> feed = new ArrayList<>();

        if(user.getLikedSongs().isEmpty()) {
            return feed;
        }

        for(String artistId : user.getTopArtists()) {
            for(Song song : getNewReleases(artistId)) {
                feed.add(new SongFeedItem(FeedEnum.NEW_RELEASE, song));
            }
        }

        for(Song song : getRecommendedSongs(user)) {
            feed.add(new SongFeedItem(FeedEnum.RECOMMENDATION, song));
        }

        feed.removeAll(user.getSeenFeed());
        Collections.shuffle(feed);

        return feed;
    }
}