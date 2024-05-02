package harmonize.DTOs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import harmonize.Entities.FeedItems.AbstractFeedItem;
import lombok.Data;

@Data
public class FeedDTO {
    private int index;
    private AbstractFeedItem item;

    @JsonCreator
    public FeedDTO(@JsonProperty("index") int index, @JsonProperty("item") AbstractFeedItem item) {
        this.index = index;
        this.item = item;
    }
}
