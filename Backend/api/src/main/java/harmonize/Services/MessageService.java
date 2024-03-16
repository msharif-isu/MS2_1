package harmonize.Services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import harmonize.DTOs.ConversationDTO;
import harmonize.DTOs.MessageDTO;
import harmonize.DTOs.UserDTO;
import harmonize.Entities.Conversation;
import harmonize.Entities.Message;
import harmonize.Entities.User;
import harmonize.Repositories.ConversationRepository;
import harmonize.Repositories.MessageRepository;
import harmonize.Security.ChatCrypto;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    ConversationRepository conversationRepository;
    private ChatCrypto chatCrypto;

    @Autowired
    public MessageService(ConversationRepository conversationRepository, MessageRepository messageRepository, ChatCrypto chatCrypto) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.chatCrypto = chatCrypto;
    }

    public Message createMessage(User sender, Conversation conversation, String text, String privateKey) {
        Message message = new Message();
        message.setTime(new Date());
        message.setSender(sender);
        message.setConversation(conversation);
        for (User user : conversation.getMembers()) {
            message.getEncryptions().put(user, text);   
        }
        conversation.getMessages().add(message);
        messageRepository.save(message);
        conversationRepository.save(conversation);
        return message;
    }

    public MessageDTO readMessage(User reciever, Message message, String privateKey) {
        return new MessageDTO(
            message.getId(),
            message.getTime(),
            new UserDTO(message.getSender()),
            new ConversationDTO(message.getConversation()),
            message.getEncryptions().get(reciever)
        );
    }
    
}
