package harmonize.DTOs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import harmonize.Entities.Message;
import lombok.Data;

/**
 * 
 * @author Isaac Denning
 * 
 */ 
@Data
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

    public MessageDTO(ConversationDTO conversation, String text) {
        this.conversation = conversation;
        this.text = text;
    }

    @JsonCreator
    public MessageDTO(
        @JsonProperty("id") int id,
        @JsonProperty("time") long time,
        @JsonProperty("sender") UserDTO sender,
        @JsonProperty("conversation") ConversationDTO conversation,
        @JsonProperty("text") String text
        ) {
        this.id = id;
        this.time = time;
        this.sender = sender;
        this.conversation = conversation;
        this.text = text;
    }
}
