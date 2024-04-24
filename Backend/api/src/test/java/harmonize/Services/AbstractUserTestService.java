package harmonize.Services;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;

import harmonize.DTOs.AuthDTO;
import harmonize.DTOs.UserDTO;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractUserTestService {
    protected int port;
    protected String url;
    @Getter @Setter protected String username;
    @Getter @Setter protected String password;
    @Getter @Setter protected UserDTO user;
    @Getter @Setter protected AuthDTO auth;

    @Getter WebSocketTestService chatSocket;

    @Autowired
    protected RequestService requestService;

    public AbstractUserTestService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setConnection(String hostname, int port) {
        this.url = "http://" + hostname + ":";
        this.port = port;
        chatSocket = new WebSocketTestService(URI.create("ws://" + hostname + ":" + port + "/chats?username=" + username + "&password=" + password));
    }
}
