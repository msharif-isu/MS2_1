package harmonize.DTOs;

import harmonize.Entities.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostDTO {
    private int id;
    private UserDTO poster;
    private String post;
    private long time;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.poster = new UserDTO(post.getPoster());
        this.post = post.getPost();
        this.time = post.getTime().getTime();
    }
}