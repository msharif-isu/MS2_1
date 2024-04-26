package harmonize.Services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import harmonize.Entities.Conversation;
import harmonize.Entities.Message;
import harmonize.Entities.User;
import harmonize.Repositories.ConversationRepository;
import harmonize.Repositories.UserRepository;

@Service
public class ConversationService {
    private ConversationRepository conversationRepository;
    private MessageService messageService;
    private UserRepository userRepository;
    private ChatService chatService;

    @Autowired
    public ConversationService(ConversationRepository conversationRepository, UserRepository userRepository, ChatService chatService, MessageService messageService) {
        this.conversationRepository = conversationRepository;
        this.messageService = messageService;
        this.userRepository = userRepository;
        this.chatService = chatService;
    }

    public Conversation createConversation(Set<User> members) {
        for (Conversation conversation : conversationRepository.findAll()) {
            if (conversation.getMembers().equals(members)) {
                return conversation;
            }
        }

        Conversation conversation = new Conversation(members);
        conversationRepository.save(conversation);
        chatService.notifyUsers(conversation);
        return conversation;
    }

    public void deleteConversation(Set<User> members) {
        for (Conversation conversation : conversationRepository.findAll()) {
            if (conversation.getMembers().equals(members)) {
                deleteConversation(conversation);
            }
        }
    }

    public void deleteConversation(Conversation conversation) {
        for (User user : conversation.getMembers()) {
            user.getConversations().remove(conversation);
            userRepository.save(user);
        }
        chatService.notifyUsers(conversation, true);
        for (Message message : conversation.getMessages()) {
            messageService.deleteMessage(message);
        }
        conversationRepository.delete(conversation);
    }

    public void removeMember(Conversation conversation, User member) {
        conversation.getMembers().remove(member);

        for (Message message : conversation.getMessages()) {
            if (message.getSender().equals(member)) {
                messageService.deleteMessage(message);
            } else {
                messageService.removeRecipient(message, member);
            }
        }
        if (conversation.getMembers().size() <= 1)
            deleteConversation(conversation);
        else
            conversationRepository.save(conversation);
    }
}
