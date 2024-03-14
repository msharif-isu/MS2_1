package harmonize.Services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import harmonize.Entities.User;
import harmonize.ErrorHandling.Exceptions.UserNotFoundException;
import harmonize.ErrorHandling.Exceptions.UsernameTakenException;
import harmonize.Repositories.RoleRepository;
import harmonize.Repositories.UserRepository;
import jakarta.websocket.Session;

@Service
public class ChatService {
    private static Map<Session, User> sessionMap = new Hashtable<>();
    private static Map<User, Session> userMap = new Hashtable<>();

    private UserRepository userRepository;

    @Autowired
    public ChatService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                broadcast("Time Hearbeat: " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    public void onOpen(Session session, String wrapperToken) throws IOException {
        User user = userRepository.findByUsername(session.getUserPrincipal().getName());
        System.out.println("Wrapper: " + wrapperToken);
        if (user == null)
            throw new UserNotFoundException(session.getUserPrincipal().getName());
        if (userMap.containsKey(user))
            throw new UsernameTakenException("User " + user.getUsername() + " has already joined the chat on a different connection.");

        sessionMap.put(session, user);
        userMap.put(user, session);

        send(user, "Welcome to the chat server, " + user.getUsername());

        broadcast("User: " + user.getUsername() + " has Joined the Chat");
    }

    public void onMessage(Session session, String message) throws IOException {
        User user = sessionMap.get(session);

        broadcast(user.getUsername() + ": " + message);
    }

    public void onClose(Session session) throws IOException {
        User user = sessionMap.get(session);

        sessionMap.remove(session);
        userMap.remove(user);

        broadcast(user.getUsername() + " disconnected");
    }

    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    private void send(User user, String message) throws IOException {
        userMap.get(user).getBasicRemote().sendText(message);
    }

    private void broadcast(String message) throws IOException {
        sessionMap.forEach((session, user) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
