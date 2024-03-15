package harmonize.DTOs;

import java.util.Date;

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
    private Date time;
    private UserDTO sender;
    private ConversationDTO conversation;
    private String text;
}
