package harmonize.Services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import harmonize.Entities.Conversation;
import harmonize.Entities.Message;
import harmonize.Entities.User;
import harmonize.Repositories.MessageRepository;
import harmonize.Security.ChatCrypto;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private ChatCrypto chatCrypto;

    @Autowired
    public MessageService(MessageRepository messageRepository, ChatCrypto chatCrypto) {
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
        messageRepository.save(message);
        return message;
    }

    public String readMessage(User reciever, Message message, String privateKey) {
        return message.getEncryptions().get(reciever);
    }
    
}
