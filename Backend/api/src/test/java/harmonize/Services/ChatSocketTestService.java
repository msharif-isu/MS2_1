package harmonize.Services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import harmonize.DTOs.ConversationDTO;
import harmonize.DTOs.MessageDTO;
import harmonize.DTOs.TransmissionDTO;
import lombok.Getter;

public class ChatSocketTestService extends WebSocketTestService {

    @Getter private List<ConversationDTO> conversations = new ArrayList<>();
    @Getter private List<MessageDTO> chats = new ArrayList<>();

    ObjectMapper mapper = new ObjectMapper();

    public ChatSocketTestService(URI serverUri) throws Exception {
        super(serverUri);
    }

    @Override
    public void onMessage(String message) {
        super.onMessage(message);
        try {
            JsonNode map = mapper.readTree(message);
            if (map.at("/type").textValue().equals(ConversationDTO.class.getName())) {
                conversations.add(mapper.readValue(map.at("/data").toString(), ConversationDTO.class));
            } else if (map.at("/type").textValue().equals(MessageDTO.class.getName())) {
                chats.add(mapper.readValue(map.at("/data").toString(), MessageDTO.class));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void send(MessageDTO message) throws JsonProcessingException {
        send(mapper.writeValueAsString(new TransmissionDTO(message.getClass(), message)));
    }
    
}
