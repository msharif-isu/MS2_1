package harmonize.Controllers;

import java.io.IOException;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import harmonize.Services.ChatService;

@ServerEndpoint(value = "/chats/{password}")
@Component
public class ChatController {
    private static ChatService chatService;

    @Autowired
    public void setChatService(ChatService chatService) {
        ChatController.chatService = chatService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "password") String password) throws IOException {
        chatService.onOpen(session, password);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        chatService.onMessage(session, message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        chatService.onClose(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        chatService.onError(session, throwable);
    }
}