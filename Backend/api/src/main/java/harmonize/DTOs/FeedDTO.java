package harmonize.DTOs;

import harmonize.Entities.Song;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedDTO {
    private String type;
    private Object body;

    public FeedDTO(String type, Song song) {
        this.type = type;
        this.body = song;
    }
}
