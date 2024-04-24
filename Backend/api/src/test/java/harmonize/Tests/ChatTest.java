package harmonize.Tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import harmonize.TestUtil;

public class ChatTest extends TestUtil {

    @Test
    public void ConnectionTest() throws Exception {
        todTestService.getChatSocket().connect();
        Thread.sleep(1000);
        assertTrue(todTestService.getChatSocket().isOpen(), "The websocket connection closed unexpectedly.");
    }
}
