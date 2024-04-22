package harmonize.DTOs;

import harmonize.Entities.FeedItems.AbstractFeedItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedDTO {
    private int index;
    private AbstractFeedItem item;
}
