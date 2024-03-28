package harmonize.Services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import harmonize.Entities.Conversation;
import harmonize.Entities.User;
import harmonize.Repositories.ConversationRepository;
import harmonize.Repositories.UserRepository;

@Service
public class ConversationService {
    private ConversationRepository conversationRepository;
    private UserRepository userRepository;
    private ChatService chatService;

    @Autowired
    public ConversationService(ConversationRepository conversationRepository, UserRepository userRepository, ChatService chatService) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.chatService = chatService;
    }

    public Conversation createConversation(Set<User> members) {
        Conversation conversation = new Conversation(members);
        conversationRepository.save(conversation);
        chatService.notifyUsers(conversation);
        return conversation;
    }

    public void deleteConversation(Set<User> members) {
        for (Conversation conversation : conversationRepository.findAll()) {
            if (conversation.getMembers().equals(members)) {
                for (User user : members) {
                    user.getConversations().remove(conversation);
                    userRepository.save(user);
                }
                chatService.notifyUsers(conversation, true);
                conversationRepository.delete(conversation);
            }
        }
    }
}
