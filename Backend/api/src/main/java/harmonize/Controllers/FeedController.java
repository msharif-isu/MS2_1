package harmonize.Controllers;

import java.io.IOException;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/feed")
@Component
public class FeedController {

    @Autowired
    public void setFeedService() {
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
    }

    @OnClose
    public void onClose(Session session) throws IOException {
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
    }
}