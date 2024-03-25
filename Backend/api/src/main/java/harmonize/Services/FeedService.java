package harmonize.Services;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;

import harmonize.DTOs.ConversationDTO;
import harmonize.DTOs.MessageDTO;
import harmonize.Entities.Conversation;
import harmonize.Entities.Message;
import harmonize.Entities.User;
import harmonize.ErrorHandling.Exceptions.InternalServerErrorException;
import harmonize.Security.ChatCrypto.Keys;
import jakarta.websocket.Session;

public class FeedService {
    public FeedService() {

    }

    public void onOpen(Session session) throws IOException {

    }

    public void onMessage(Session session, String message) throws IOException {

    }

    public void onClose(Session session) throws IOException {

    }

    public void onError(Session session, Throwable throwable) {

    }
}
