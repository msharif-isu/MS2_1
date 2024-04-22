package harmonize.Services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import harmonize.Entities.Conversation;
import harmonize.Entities.User;
import harmonize.Repositories.ConversationRepository;

@Service
public class ConversationService {
    private ConversationRepository conversationRepository;
    private ChatService chatService;

    @Autowired
    public ConversationService(ConversationRepository conversationRepository, ChatService chatService) {
        this.conversationRepository = conversationRepository;
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
                chatService.notifyUsers(conversation, true);
                conversationRepository.delete(conversation);
            }
        }
    }
}
