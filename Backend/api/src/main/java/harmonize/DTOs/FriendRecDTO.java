package harmonize.DTOs;

import harmonize.Entities.Artist;
import harmonize.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class FriendRecDTO {
    @NonNull
    private UserDTO user;

    @NonNull
    private ArtistDTO artist;

    public FriendRecDTO(User user, Artist artist) {
        this.user = new UserDTO(user);
        this.artist = new ArtistDTO(artist);
    }
}