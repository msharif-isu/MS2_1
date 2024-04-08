package harmonize.Services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import harmonize.DTOs.FeedDTO;
import harmonize.DTOs.TransmissionDTO;
import harmonize.Entities.LikedSong;
import harmonize.Entities.Song;
import harmonize.Entities.User;
import harmonize.Entities.FeedItems.AbstractFeedItem;
import harmonize.Entities.FeedItems.SongFeedItem;
import harmonize.Enum.FeedEnum;
import harmonize.ErrorHandling.Exceptions.EntityNotFoundException;
import harmonize.ErrorHandling.Exceptions.InternalServerErrorException;
import harmonize.ErrorHandling.Exceptions.UnauthorizedException;
import harmonize.Repositories.UserRepository;
import harmonize.Repositories.FeedRepositories.FeedRepository;
import harmonize.Repositories.FeedRepositories.SongFeedRepository;
import jakarta.websocket.Session;

@Service
public class FeedService {
    private static Set<Session> sessions = new HashSet<>();

    private Map<Class<? extends AbstractFeedItem>, FeedRepository> feedRepositories;

    private FeedRepository feedRepository;
    private MusicService musicService;
    private UserRepository userRepository;
    private ObjectMapper mapper;
    private BCryptPasswordEncoder encoder;

    @Autowired
    public FeedService(MusicService musicService, UserRepository userRepository, SongFeedRepository songFeedRepository, FeedRepository feedRepository, 
                            BCryptPasswordEncoder encoder, ObjectMapper mapper) {
        this.musicService = musicService;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.encoder = encoder;

        this.feedRepository = feedRepository;

        this.feedRepositories = new HashMap<>();
        this.feedRepositories.put(SongFeedItem.class, songFeedRepository);
    }

    public void onOpen(Session session) throws IOException {
        loadProperties(session);
        sessions.add(session);
    }

    public void onMessage(Session session, String message) throws IOException {
        loadProperties(session);
        List<AbstractFeedItem> feed = ((List<?>)session.getUserProperties().get("feed")).stream().map(AbstractFeedItem.class::cast).collect(Collectors.toList());
        User user = (User)session.getUserProperties().get("user");

        if(feed.isEmpty()) {
            send(session, FeedEnum.NO_DATA);
            return;
        }
        
        JsonNode jsonMessage = mapper.readTree(message);

        if (!jsonMessage.at("/type").textValue().equals(FeedDTO.class.getName())) {
            onError(session, new InternalServerErrorException("Could not parse message."), false);
            return;
        }

        if(jsonMessage.at("/body/requestType").asInt() == FeedEnum.REFRESH.ordinal()) {
            session.getUserProperties().remove("feed");
            session.getUserProperties().remove("user");
            loadProperties(session);
            return;
        }

        if(jsonMessage.at("/body/requestType").asInt() == FeedEnum.NEW_PAGE.ordinal()) {  
            int offset = jsonMessage.at("/body/data/offset").asInt();
            int limit = jsonMessage.at("/body/data/limit").asInt();

            if(limit <= 0 || offset < 0) {
                onError(session, new IndexOutOfBoundsException("Page and limit must be positive"), false);
                return;
            }

            if(offset >= feed.size() || limit > feed.size()) {
                onError(session, new IndexOutOfBoundsException("Requested item is out of bounds"), false);
                return;
            }

            int start = offset;
            int end = Math.min(feed.size(), offset + limit);

            List<AbstractFeedItem> feedPage = feed.subList(start, end);
            for(int i = start; i < end; i++) {
                AbstractFeedItem item = feedPage.get(i - start);
                if(item instanceof SongFeedItem)
                    musicService.getSong(((SongFeedItem)item).getSong().getId());

                feedRepositories.get(item.getClass()).save(item);
                user.getSeenFeed().add(item);

                send(session, new FeedDTO(i, item));
            }
        }

        session.getUserProperties().put("feed", feed);
    }

    public void onClose(Session session) throws IOException {
        loadProperties(session);
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
            if (closeSession) {
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron="0 0 0 * * ?") //Midnight
    public void removeExpiredFeedItems() {
        List<AbstractFeedItem> list = feedRepository.getExpiredFeedItems();

        if(list != null)
            feedRepository.deleteAll(list);
    }

    private void send(Session session, Object obj) throws IOException {
        session.getBasicRemote().sendText(mapper.writeValueAsString(new TransmissionDTO(obj.getClass(), obj)));
    }

    private void loadProperties(Session session) throws IOException {
        User user;
        String password;

        if (!session.getRequestParameterMap().containsKey("password"))
            onError(session, new UnauthorizedException("Password field in request parameters was empty."));
        
        if (!session.getRequestParameterMap().containsKey("username"))
            onError(session, new UnauthorizedException("Username field in request parameters was empty."));

        user = 
            session.getUserProperties().containsKey("user") ?
                (User)session.getUserProperties().get("user") :
                userRepository.findByUsername(session.getRequestParameterMap().get("username").get(0));
            
        if (user == null) {
            onError(session, new EntityNotFoundException("User " + session.getRequestParameterMap().get("username").get(0) + " not found."));
            return;
        }

        password = 
            session.getUserProperties().containsKey("password") ? 
                (String)session.getUserProperties().get("password") : 
                session.getRequestParameterMap().get("password").get(0);
        
        if (!encoder.matches(password, user.getPassword()))
            onError(session, new UnauthorizedException("Password field in request parameters was invalid."));

        List<AbstractFeedItem> feed;
        if(session.getUserProperties().containsKey("feed")) 
            feed = ((List<?>)session.getUserProperties().get("feed")).stream().map(AbstractFeedItem.class::cast).collect(Collectors.toList());
        else {
            feed = generateFeed(user);
            ObjectNode node = mapper.createObjectNode();
            node.put("size", feed.size());
            send(session, node);
        }

        session.getUserProperties().put("user", user);
        session.getUserProperties().put("password", password);
        session.getUserProperties().put("feed", feed);
    }

    private List<AbstractFeedItem> generateFeed(User user) {
        List<AbstractFeedItem> feed = new ArrayList<>();

        if(user.getLikedSongs().isEmpty()) {
            return feed;
        }

        for(String artistId : user.getTopArtists()) {
            for(Song song : musicService.getNewReleases(artistId)) {
                if(user.getLikedSongs().contains(new LikedSong(user, song)))
                    continue;
                feed.add(new SongFeedItem(FeedEnum.NEW_RELEASE, song, user));
            }
        }

        for(Song song : musicService.getRecommendedSongs(user)) {
            if(user.getLikedSongs().contains(new LikedSong(user, song)))
                continue;
            feed.add(new SongFeedItem(FeedEnum.RECOMMENDATION, song, user));
        }

        feed.removeAll(user.getSeenFeed());
        Collections.shuffle(feed);

        return feed;
    }
}