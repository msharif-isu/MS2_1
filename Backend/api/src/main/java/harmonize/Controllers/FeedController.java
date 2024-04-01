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

import harmonize.Services.FeedService;

@ServerEndpoint(value = "/feed")
@Component
public class FeedController {
    private static FeedService feedService;

    @Autowired
    public void setFeedService(FeedService feedService) {
        FeedController.feedService = feedService;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        feedService.onOpen(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        feedService.onMessage(session, message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        feedService.onClose(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        feedService.onError(session, throwable);
    }
}