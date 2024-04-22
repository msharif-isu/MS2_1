package harmonize.DTOs;

import harmonize.Entities.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author Isaac Denning
 * 
 */ 
@Data
@AllArgsConstructor
public class MessageDTO {
    private int id;
    private long time;
    private UserDTO sender;
    private ConversationDTO conversation;
    private String text;

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.time = message.getTime().getTime();
        this.sender = new UserDTO(message.getSender());
        this.conversation = new ConversationDTO(message.getConversation());
        this.text = null;
    }
}
