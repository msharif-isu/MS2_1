package harmonize.DTOs;

import harmonize.Entities.Post;
import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PostDTO {
    private int id;
    
    private UserDTO poster;

    @NonNull
    private String post;

    private long time;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.poster = new UserDTO(post.getPoster());
        this.post = post.getPost();
        this.time = post.getTime().getTime();
    }
}