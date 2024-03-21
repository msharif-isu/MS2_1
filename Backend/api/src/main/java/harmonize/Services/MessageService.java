package harmonize.Services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import harmonize.DTOs.ConversationDTO;
import harmonize.DTOs.MessageDTO;
import harmonize.DTOs.UserDTO;
import harmonize.Entities.Conversation;
import harmonize.Entities.Message;
import harmonize.Entities.Report;
import harmonize.Entities.User;
import harmonize.ErrorHandling.Exceptions.MessageNotFoundException;
import harmonize.Repositories.ConversationRepository;
import harmonize.Repositories.MessageRepository;
import harmonize.Security.ChatCrypto;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private ConversationRepository conversationRepository;
    private ReportService reportService;
    private ChatCrypto chatCrypto;

    @Autowired
    public MessageService(ConversationRepository conversationRepository, MessageRepository messageRepository, ReportService reportService, ChatCrypto chatCrypto) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.reportService = reportService;
        this.chatCrypto = chatCrypto;
    }

    public Message createMessage(User sender, Conversation conversation, String text, String privateKey) throws Exception {
        Message message = new Message();
        message.setTime(new Date());
        message.setSender(sender);
        message.setConversation(conversation);
        for (User user : conversation.getMembers())
            message.getEncryptions().put(user, chatCrypto.encrypt(privateKey, user.getPublicKey(), text));   
        conversation.getMessages().add(message);
        messageRepository.save(message);
        conversationRepository.save(conversation);
        return message;
    }

    public MessageDTO readMessage(User reciever, Message message, String privateKey) throws Exception {
        return new MessageDTO(
            message.getId(),
            message.getTime(),
            new UserDTO(message.getSender()),
            new ConversationDTO(message.getConversation()),
            chatCrypto.decrypt(privateKey, message.getSender().getPublicKey(), message.getEncryptions().get(reciever))
        );
    }

    public String deleteMessage(int id) {
        Message message = messageRepository.findReferenceById(id);
        if (message == null)
            throw new MessageNotFoundException(id);
        Conversation conversation = message.getConversation();
        
        conversation.getMessages().remove(message);
        for (Report report : message.getReports()) {
            reportService.deleteReport(report);
        }

        conversationRepository.save(conversation);
        messageRepository.delete(message);
        
        return new String(String.format("Message %d was deleted.", message.getId()));
    }
    
}
