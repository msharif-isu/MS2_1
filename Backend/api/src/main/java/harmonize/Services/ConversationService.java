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

    @Autowired
    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Conversation createChat(Set<User> members) {
        Conversation chat = new Conversation(members);
        conversationRepository.save(chat);
        return chat;
    }

    public void deleteChat(Set<User> members) {
        for (Conversation chat : conversationRepository.findAll()) {
            if (chat.getMembers().equals(members)) {
                for (User user : members) {
                    user.getConversations().remove(chat);
                }
                conversationRepository.delete(chat); 
            }
        }
    }
}
