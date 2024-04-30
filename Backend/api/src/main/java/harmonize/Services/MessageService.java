package harmonize.Services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import harmonize.DTOs.ConversationDTO;
import harmonize.DTOs.MessageDTO;
import harmonize.DTOs.ResponseDTO;
import harmonize.DTOs.UserDTO;
import harmonize.Entities.Conversation;
import harmonize.Entities.Message;
import harmonize.Entities.Report;
import harmonize.Entities.User;
import harmonize.ErrorHandling.Exceptions.EntityNotFoundException;
import harmonize.Repositories.ConversationRepository;
import harmonize.Repositories.MessageRepository;
import harmonize.Security.ChatCrypto;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private ConversationRepository conversationRepository;
    private ReportService reportService;
    private ChatCrypto chatCrypto;
    private BCryptPasswordEncoder encoder;

    @Autowired
    public MessageService(ConversationRepository conversationRepository, MessageRepository messageRepository, ReportService reportService, ChatCrypto chatCrypto, BCryptPasswordEncoder encoder) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.reportService = reportService;
        this.chatCrypto = chatCrypto;
        this.encoder = encoder;
    }

    public Message createMessage(User sender, Conversation conversation, String text, String privateKey) throws Exception {
        Message message = new Message();
        message.setTime(new Date());
        message.setSender(sender);
        message.setHash(encoder.encode(text));
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
            message.getTime().getTime(),
            new UserDTO(message.getSender()),
            new ConversationDTO(message.getConversation()),
            chatCrypto.decrypt(privateKey, message.getSender().getPublicKey(), message.getEncryptions().get(reciever))
        );
    }

    public ResponseDTO deleteMessage(int id) {
        Message message = messageRepository.findReferenceById(id);
        if (message == null)
            throw new EntityNotFoundException("Message " + id + " not found.");
        return deleteMessage(message);
    }

    public ResponseDTO deleteMessage(Message message) {
        Conversation conversation = message.getConversation();
        
        conversation.getMessages().remove(message);
        for (Report report : message.getReports()) {
            reportService.deleteReport(report);
        }

        conversationRepository.save(conversation);
        messageRepository.delete(message);
        
        return new ResponseDTO(String.format("Message %d was deleted.", message.getId()));
    }

    public void removeRecipient(Message message, User recipient) {
        message.getEncryptions().remove(recipient);
        messageRepository.save(message);
    }
    
}
