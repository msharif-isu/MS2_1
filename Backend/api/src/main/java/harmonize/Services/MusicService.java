package harmonize.Services;

import java.time.Year;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import harmonize.DTOs.SongRecDTO;
import harmonize.DTOs.SearchDTO;
import harmonize.Entities.Artist;
import harmonize.Entities.ArtistFreq;
import harmonize.Entities.LikedSong;
import harmonize.Entities.Song;
import harmonize.Entities.User;
import harmonize.ErrorHandling.Exceptions.InvalidArgumentException;
import harmonize.Repositories.ArtistRepository;
import harmonize.Repositories.SongRepository;

@Service
public class MusicService {
    private String apiAuthentication;

    private long apiExpiration;

    private String apiURL;

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    private SongRepository songRepository;

    private ArtistRepository artistRepository;

    @Autowired
    public MusicService(RestTemplate restTemplate, SongRepository songRepository, ArtistRepository artistRepository) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
        this.apiURL = "https://api.spotify.com/v1";
        this.apiExpiration = 0;
        this.songRepository = songRepository;
        this.artistRepository = artistRepository;
    }

    public String getAPIToken() {
        if (apiExpiration > System.currentTimeMillis())
            return apiAuthentication;

        final String url = "https://accounts.spotify.com/api/token";
        final String clientID = "d3eacb5996b54e6fbb79ae2c4902a417";
        final String clientSecret = "4838a780565949b6b02e6f4f97f4ba0b";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String encodedAuth = Base64.getEncoder().encodeToString(String.format("%s:%s", clientID, clientSecret).getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);
        
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        JsonNode responseJson;

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            responseJson = objectMapper.readTree(response.getBody());
        }
        catch(Exception e) {
            throw new InvalidArgumentException("Unable to retrieve token.");
        }

        this.apiExpiration = System.currentTimeMillis() + responseJson.get("expires_in").asInt() * 1000;

        return (apiAuthentication = responseJson.get("access_token").asText());
    }

    public JsonNode search(SearchDTO search) {
        String urlEnd = "/search";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + getAPIToken());

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(apiURL + urlEnd)
                        .queryParam("q", search.getQ())
                        .queryParam("type", search.getType())
                        .queryParam("market", "US");

        if (search.getLimit() != null)
            urlBuilder.queryParam("limit", search.getLimit());

        if (search.getOffset() != null)
            urlBuilder.queryParam("offset", search.getOffset());

        String url = urlBuilder.encode().toUriString();
        JsonNode responseJson;

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            responseJson = objectMapper.readTree(response.getBody());
        } catch(Exception e) {
            throw new InvalidArgumentException("Invalid search.");
        }

        return responseJson;
    }

    public JsonNode getSong(String id) {
        String urlEnd = "/tracks/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + getAPIToken());

        JsonNode responseJson;

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiURL + urlEnd, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            responseJson = objectMapper.readTree(response.getBody());
        } catch(Exception e) {
            throw new InvalidArgumentException("Invalid song.");
        }
        
        Artist artist = new Artist(responseJson.get("artists").get(0));
        Song song = new Song(responseJson, artistRepository.save(artist));
        song.setArtist(artist);
        artist.getSongs().add(song);

        songRepository.save(song);

        return responseJson;
    }

    public JsonNode getArtist(String id) {
        String urlEnd = "/artists/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + getAPIToken());

        JsonNode responseJson;

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiURL + urlEnd, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            responseJson = objectMapper.readTree(response.getBody());
        } catch(Exception e) {
            throw new InvalidArgumentException("Invalid artist.");
        }

        artistRepository.save(new Artist(responseJson));

        return responseJson;
    }

    public JsonNode getArtistAlbums(SearchDTO search) {
        String urlEnd = "/artists/" + search.getQ() + "/albums";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + getAPIToken());

        String url = UriComponentsBuilder.fromHttpUrl(apiURL + urlEnd)
                        .queryParam("include_groups", search.getType())
                        .queryParam("market", "US")
                        .queryParam("limit", search.getLimit())
                        .queryParam("offset", search.getOffset())
                        .encode().toUriString();

        JsonNode responseJson;

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            responseJson = objectMapper.readTree(response.getBody());
        } catch(Exception e) {
            throw new InvalidArgumentException("Invalid search");
        }

        return responseJson;
    }

    public JsonNode getAlbumSongs(SearchDTO search) {
        String urlEnd = "/albums/" + search.getQ() + "/tracks";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + getAPIToken());

        String url = UriComponentsBuilder.fromHttpUrl(apiURL + urlEnd)
                        .queryParam("market", "US")
                        .queryParam("limit", search.getLimit())
                        .queryParam("offset", search.getOffset())
                        .encode().toUriString();

        JsonNode responseJson;

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            responseJson = objectMapper.readTree(response.getBody());
        } catch(Exception e) {
            throw new InvalidArgumentException("Invalid search");
        }

        return responseJson;
    }

    public JsonNode getRecommendations(SongRecDTO recommendation) {
        String urlEnd = "/recommendations";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + getAPIToken());

        String url = UriComponentsBuilder.fromHttpUrl(apiURL + urlEnd)
                        .queryParam("limit", recommendation.getLimit())
                        .queryParam("market", "US")
                        .queryParam("seed_artists", String.join(",", recommendation.getArtistIds()))
                        .queryParam("seed_tracks", String.join(",", recommendation.getSongIds()))
                        .encode().toUriString();

        JsonNode responseJson;

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            responseJson = objectMapper.readTree(response.getBody());
        } catch(Exception e) {
            throw new InvalidArgumentException("Invalid search");
        }

        return responseJson;
    }

    public List<Song> getNewReleases(String artistId) {
        int limit = 50;
        int offset = 0;
        List<Song> newReleases = new ArrayList<>();
        JsonNode albums;

        do {
            SearchDTO search = new SearchDTO(artistId, "album,single", Integer.toString(limit), Integer.toString(offset));
            albums = getArtistAlbums(search);

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
            SearchDTO search = new SearchDTO(albumId, "track", Integer.toString(limit), Integer.toString(offset));
            album = getAlbumSongs(search);

            for(int i = 0; i < album.get("items").size(); i++)
                songs.add(new Song(album.get("items").get(i)));

            offset += limit;
        } while(album.get("items").size() == limit);

        return songs;
    }

    public List<Song> getRecommendedSongs(User user) {
        List<Song> songRec = new ArrayList<>();
        List<String> artistIds = new ArrayList<>();
        List<String> songIds = new ArrayList<>();
        List<ArtistFreq> topArtists = user.getTopArtists();
        List<LikedSong> likedSongs = user.getLikedSongs();

        if(topArtists.isEmpty() || likedSongs.isEmpty())
            return songRec;

        for(int i = 0; i < topArtists.size() && i < 3; i++)
            artistIds.add(topArtists.get(i).getArtist().getId());

        for(int i = 0; i < likedSongs.size() && i < 2; i++)
            songIds.add(likedSongs.get(i).getSong().getId());

        JsonNode recommendations = getRecommendations(new SongRecDTO(Integer.toString(100), artistIds, songIds));
        
        for(int i = 0; i < recommendations.get("tracks").size(); i++)
            songRec.add(new Song(recommendations.get("tracks").get(i)));

        return songRec;
    }
}
