package harmonize.Tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import harmonize.TestUtil;
import harmonize.DTOs.ConversationDTO;
import harmonize.DTOs.MessageDTO;
import harmonize.Services.WebSocketTestService;

public class ChatTest extends TestUtil {

    @Test
    @DisabledIfEnvironmentVariable(named = "DOCKER_RUNNING", matches = "true")
    public void connectionOkTest() throws Exception {
        todTestService.getChatSocket().connect();
        Thread.sleep(5000);
        assertTrue(todTestService.getChatSocket().isOpen());
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "DOCKER_RUNNING", matches = "true")
    public void connectionInvalidPasswordTest() throws Exception {
        chatSocket = new WebSocketTestService(URI.create("ws://" + getHostname() + ":" + getPort() + "/chats?username=" + todTestService.getUsername() + "&password=INVALIDPASSWORD"));
        Thread.sleep(5000);
        assertFalse(todTestService.getChatSocket().isOpen());
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "DOCKER_RUNNING", matches = "true")
    public void connectionInvalidUsernameTest() throws Exception {
        chatSocket = new WebSocketTestService(URI.create("ws://" + getHostname() + ":" + getPort() + "/chats?username=INVALIDUSERNAME" + "&password=" + todTestService.getPassword()));
        Thread.sleep(5000);
        assertFalse(todTestService.getChatSocket().isOpen());
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "DOCKER_RUNNING", matches = "true")
    public void recieveConversationOkTest() throws Exception {
        todTestService.getChatSocket().connect();
        todTestService.addFriend(bobTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        Thread.sleep(5000);

        assertTrue(todTestService.getChatSocket().isOpen());
        assertTrue(todTestService.getChatSocket().getConversations().stream()
            .anyMatch(convo -> (convo.getMembers().containsAll(Set.of(todTestService.getUser(), bobTestService.getUser()))))
        );
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "DOCKER_RUNNING", matches = "true")
    public void recieveConversationHiddenTest() throws Exception {
        todTestService.getChatSocket().connect();
        samTestService.getChatSocket().connect();

        todTestService.addFriend(bobTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        Thread.sleep(5000);

        assertTrue(samTestService.getChatSocket().isOpen());
        assertFalse(samTestService.getChatSocket().getConversations().stream()
            .anyMatch(convo -> (convo.getMembers().containsAll(Set.of(todTestService.getUser(), bobTestService.getUser()))))
        );
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "DOCKER_RUNNING", matches = "true")
    public void recieveMessageOkTest() throws Exception {
        todTestService.getChatSocket().connect();
        bobTestService.getChatSocket().connect();
        samTestService.getChatSocket().connect();

        todTestService.addFriend(bobTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        Thread.sleep(5000);

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

    @Test
    @DisabledIfEnvironmentVariable(named = "DOCKER_RUNNING", matches = "true")
    public void groupRecieveMessageOkTest() throws Exception {
        todTestService.getChatSocket().connect();
        bobTestService.getChatSocket().connect();
        samTestService.getChatSocket().connect();

        todTestService.addFriend(bobTestService.getUser().getId());
        todTestService.addFriend(samTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        samTestService.addFriend(todTestService.getUser().getId());

        ConversationDTO conversation = todTestService.createConversation(List.of(
            todTestService.getUser().getId(), 
            bobTestService.getUser().getId(), 
            samTestService.getUser().getId()))
            .getBody();

        if (conversation == null) {
            fail();
            return;
        }
        Thread.sleep(5000);
        
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
        assertTrue(samTestService.getChatSocket().getChats().stream()
            .anyMatch(item -> (item.getText().equals(text)))
        );
    }
}
