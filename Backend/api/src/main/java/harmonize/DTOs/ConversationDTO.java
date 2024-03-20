package harmonize.DTOs;

import java.util.HashSet;
import java.util.Set;

import harmonize.Entities.Conversation;
import harmonize.Entities.User;
import lombok.Data;

/**
 * 
 * @author Isaac Denning
 * 
 */ 
@Data
public class ConversationDTO {
    private int id;
    private Set<UserDTO> members = new HashSet<>();

    public ConversationDTO(Conversation conversation) {
        id = conversation.getId();
        for (User user : conversation.getMembers()) {
            members.add(new UserDTO(user));
        }
    }
}
