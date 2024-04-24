package harmonize.Tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;

import org.junit.jupiter.api.Test;

import harmonize.TestUtil;
import harmonize.Services.WebSocketTestService;

public class ChatTest extends TestUtil {

    @Test
    public void ConnectionOkTest() throws Exception {
        todTestService.getChatSocket().connect();
        Thread.sleep(1000);
        assertTrue(todTestService.getChatSocket().isOpen());
    }

    @Test
    public void ConnectionInvalidPasswordTest() throws Exception {
        chatSocket = new WebSocketTestService(URI.create("ws://" + getHostname() + ":" + getPort() + "/chats?username=" + todTestService.getUsername() + "&password=INVALIDPASSWORD"));
        Thread.sleep(1000);
        assertFalse(todTestService.getChatSocket().isOpen());
    }

    @Test
    public void ConnectionInvalidUsernameTest() throws Exception {
        chatSocket = new WebSocketTestService(URI.create("ws://" + getHostname() + ":" + getPort() + "/chats?username=INVALIDUSERNAME" + "&password=" + todTestService.getPassword()));
        Thread.sleep(1000);
        assertFalse(todTestService.getChatSocket().isOpen());
    }
}
