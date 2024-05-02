package harmonize.DTOs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import harmonize.Entities.Post;
import io.micrometer.common.lang.NonNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostDTO {
    private int id;
    
    private UserDTO poster;

    @NonNull
    private String post;

    private long time;

    @JsonCreator
    public PostDTO(@JsonProperty("id") int id, @JsonProperty("poster") UserDTO poster, @JsonProperty("post") String post, @JsonProperty("time") long time) {
        this.id = id;
        this.poster = poster;
        this.post = post;
        this.time = time;
    }

    public PostDTO(Post post) {
        this.id = post.getId();
        this.poster = new UserDTO(post.getPoster());
        this.post = post.getPost();
        this.time = post.getTime().getTime();
    }
}