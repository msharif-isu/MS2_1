package harmonize.Services;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import harmonize.DTOs.ConversationDTO;
import harmonize.DTOs.MessageDTO;
import harmonize.DTOs.TransmissionDTO;
import harmonize.Entities.Conversation;
import harmonize.Entities.Message;
import harmonize.Entities.User;
import harmonize.ErrorHandling.Exceptions.EntityNotFoundException;
import harmonize.ErrorHandling.Exceptions.InternalServerErrorException;
import harmonize.ErrorHandling.Exceptions.InvalidArgumentException;
import harmonize.ErrorHandling.Exceptions.UnauthorizedException;
import harmonize.Repositories.ConversationRepository;
import harmonize.Repositories.UserRepository;
import harmonize.Security.ChatCrypto;
import harmonize.Security.ChatCrypto.Keys;
import jakarta.websocket.Session;

@Service
public class ChatService {
    private static Set<Session> sessions = new HashSet<>();

    private UserRepository userRepository;
    private ConversationRepository conversationRepository;
    private MessageService messageService;
    private BCryptPasswordEncoder encoder;
    private ChatCrypto chatCrypto;
    private ObjectMapper mapper;

    @Autowired
    public ChatService(UserRepository userRepository, ConversationRepository conversationRepository, MessageService messageService, BCryptPasswordEncoder encoder, ChatCrypto chatCrypto) {
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.messageService = messageService;
        this.encoder = encoder;
        this.chatCrypto = chatCrypto;
        this.mapper = new ObjectMapper();
    }

    public void onOpen(Session session) throws IOException {
        loadProperties(session);
        User user = (User)session.getUserProperties().get("user");
        Keys keys = (Keys)session.getUserProperties().get("keys");

        for (Conversation conversation : user.getConversations()) {
            send(session, new ConversationDTO(conversation));
            for (Message message : conversation.getMessages()) {
                try {
                    send(session, messageService.readMessage(user, message, keys.getPrivateKey()));
                } catch (Exception e) {
                    onError(session, e, false);
                    e.printStackTrace();
                }   
            }
        }

        sessions.add(session);
    }

    public void onMessage(Session session, String message) throws IOException {
        loadProperties(session);
        User user = (User)session.getUserProperties().get("user");
        Keys keys = (Keys)session.getUserProperties().get("keys");

        JsonNode map = mapper.readTree(message);
        if (map.at("/type").textValue().isEmpty()) {
            onError(session, new InvalidArgumentException("Expected type field in message."), false);
            return;
        }
        if (!map.at("/type").textValue().equals(MessageDTO.class.getName())) {
            onError(session, new InternalServerErrorException("Could not parse message."), false);
            return;
        }
        if (!conversationRepository.findReferenceById(map.at("/data/conversation/id").asInt()).getMembers().contains(user)) {
            onError(session, new UnauthorizedException("You are not a member of that conversation."), false);
            return;
        }
        
        try {
            notifyUsers(messageService.createMessage(
                user,
                conversationRepository.findReferenceById(map.at("/data/conversation/id").asInt()),
                map.at("/data/text").asText(),
                keys.getPrivateKey()
            ));
        } catch (Exception e) {
            onError(session, e, false);
            e.printStackTrace();
        }
    }

    public void onClose(Session session) throws IOException {
        sessions.remove(session);
    }

    public void onError(Session session, Throwable throwable) {
        onError(session, throwable, true);
    }

    public void onError(Session session, Throwable throwable, Boolean closeSession) {
        try {
            send(session, throwable);
            if (closeSession)
                session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notifyUsers(Message message) {
        for (Session session : sessions) {
            User user = (User)session.getUserProperties().get("user");
            if (!message.getConversation().getMembers().contains(user))
                continue;

            try {
                send(session, messageService.readMessage(user, message, ((Keys)session.getUserProperties().get("keys")).getPrivateKey()));
            } catch (Exception e) {
                onError(session, e, false);
                e.printStackTrace();
            }
        }
    }

    public void notifyUsers(Conversation conversation) {
        notifyUsers(conversation, false);
    }

    public void notifyUsers(Conversation conversation, Boolean isDeleted) {
        for (Session session : sessions) {
            User user = (User)session.getUserProperties().get("user");
            if (!conversation.getMembers().contains(user))
                continue;

            try {
                ConversationDTO convoDTO = new ConversationDTO(conversation);
                if (isDeleted)
                    convoDTO.getMembers().clear();
                send(session, convoDTO);
            } catch (IOException e) {
                onError(session, e, false);
                e.printStackTrace();
            }
        }
    }

    private void send(Session session, Object obj) throws IOException {
        session.getBasicRemote().sendText(mapper.writeValueAsString(new TransmissionDTO(obj.getClass(), obj)));
    }

    private void loadProperties(Session session) {
        User user;
        String wrapperToken;
        Keys keys = null;

        if (!session.getRequestParameterMap().containsKey("password"))
            onError(session, new UnauthorizedException("Password field in request parameters was empty."));
        if (!session.getRequestParameterMap().containsKey("username"))
            onError(session, new UnauthorizedException("Username field in request parameters was empty."));

        user = 
            session.getUserProperties().containsKey("user") ?
                (User)session.getUserProperties().get("user") :
                userRepository.findByUsername(session.getRequestParameterMap().get("username").get(0));
        if (user == null) {
            onError(session, new EntityNotFoundException("User " + session.getRequestParameterMap().get("username").get(0) + " not found."));
            return;
        }

        wrapperToken = 
            session.getUserProperties().containsKey("wrapperToken") ? 
                (String)session.getUserProperties().get("wrapperToken") : 
                session.getRequestParameterMap().get("password").get(0);
        
        if (!encoder.matches(wrapperToken, user.getPassword()))
            onError(session, new UnauthorizedException("Password field in request parameters was invalid."));

        try {
            keys = 
                session.getUserProperties().containsKey("keys") ?
                    (Keys)session.getUserProperties().get("keys") :
                    chatCrypto.new Keys(user.getPublicKey(), chatCrypto.unwrap(wrapperToken, user.getPrivateKeyWrapped()));
        } catch (Exception e) {
            onError(session, new InternalServerErrorException("Could not unwrap keys."));
        }

        session.getUserProperties().put("user", user);
        session.getUserProperties().put("wrapperToken", wrapperToken);
        session.getUserProperties().put("keys", keys);
    }
}
