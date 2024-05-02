package harmonize.DTOs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import harmonize.Entities.Artist;
import harmonize.Entities.User;
import lombok.Data;
import lombok.NonNull;

@Data
public class FriendRecDTO {
    @NonNull
    private UserDTO user;

    @NonNull
    private ArtistDTO artist;

    @JsonCreator
    public FriendRecDTO(@JsonProperty("user") UserDTO user, @JsonProperty("artist") ArtistDTO artist) {
        this.user = user;
        this.artist = artist;
    }

    public FriendRecDTO(User user, Artist artist) {
        this.user = new UserDTO(user);
        this.artist = new ArtistDTO(artist);
    }
}
