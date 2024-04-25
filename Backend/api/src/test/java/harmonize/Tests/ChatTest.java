package harmonize.Tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Set;

import org.junit.jupiter.api.Test;

import harmonize.TestUtil;
import harmonize.DTOs.ConversationDTO;
import harmonize.DTOs.MessageDTO;
import harmonize.Services.WebSocketTestService;

public class ChatTest extends TestUtil {

    @Test
    public void ConnectionOkTest() throws Exception {
        todTestService.getChatSocket().connect();
        Thread.sleep(5000);
        assertTrue(todTestService.getChatSocket().isOpen());
    }

    @Test
    public void ConnectionInvalidPasswordTest() throws Exception {
        chatSocket = new WebSocketTestService(URI.create("ws://" + getHostname() + ":" + getPort() + "/chats?username=" + todTestService.getUsername() + "&password=INVALIDPASSWORD"));
        Thread.sleep(5000);
        assertFalse(todTestService.getChatSocket().isOpen());
    }

    @Test
    public void ConnectionInvalidUsernameTest() throws Exception {
        chatSocket = new WebSocketTestService(URI.create("ws://" + getHostname() + ":" + getPort() + "/chats?username=INVALIDUSERNAME" + "&password=" + todTestService.getPassword()));
        Thread.sleep(5000);
        assertFalse(todTestService.getChatSocket().isOpen());
    }

    @Test
    public void RecieveConversationOkTest() throws Exception {
        todTestService.addFriend(bobTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        Thread.sleep(5000);

        todTestService.getChatSocket().connect();

        assertTrue(todTestService.getChatSocket().isOpen());
        assertTrue(todTestService.getChatSocket().getConversations().stream()
            .anyMatch(convo -> (convo.getMembers().containsAll(Set.of(todTestService.getUser(), bobTestService.getUser()))))
        );
    }

    @Test
    public void RecieveConversationHiddenTest() throws Exception {
        todTestService.addFriend(bobTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        Thread.sleep(5000);

        todTestService.getChatSocket().connect();
        samTestService.getChatSocket().connect();

        assertTrue(samTestService.getChatSocket().isOpen());
        assertFalse(samTestService.getChatSocket().getConversations().stream()
            .anyMatch(convo -> (convo.getMembers().containsAll(Set.of(todTestService.getUser(), bobTestService.getUser()))))
        );
    }

    @Test
    public void RecieveMessageOkTest() throws Exception {
        todTestService.addFriend(bobTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        Thread.sleep(5000);
        
        todTestService.getChatSocket().connect();
        bobTestService.getChatSocket().connect();
        samTestService.getChatSocket().connect();

        ConversationDTO conversation = todTestService.getChatSocket().getConversations().stream()
            .filter(item -> (item.getMembers().containsAll(Set.of(todTestService.getUser(), bobTestService.getUser()))))
            .findAny()
            .get();
        
        String text = "Hello, World!";
        todTestService.getChatSocket().send(new MessageDTO(conversation, text));
        Thread.sleep(5000);

        assertTrue(todTestService.getChatSocket().isOpen());
        assertTrue(bobTestService.getChatSocket().isOpen());
        assertTrue(samTestService.getChatSocket().isOpen());
        assertTrue(bobTestService.getChatSocket().getChats().stream()
            .anyMatch(item -> (item.getText().equals(text)))
        );
        assertTrue(todTestService.getChatSocket().getChats().stream()
            .anyMatch(item -> (item.getText().equals(text)))
        );
        assertFalse(samTestService.getChatSocket().getChats().stream()
            .anyMatch(item -> (item.getText().equals(text)))
        );
    }

    
}
