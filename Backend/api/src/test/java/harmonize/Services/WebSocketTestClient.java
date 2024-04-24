package harmonize.Services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import lombok.Getter;

public class WebSocketTestClient extends WebSocketClient {

    @Getter private List<Exception> exceptions = new ArrayList<>();
    @Getter private List<String> messages = new ArrayList<>();

    public WebSocketTestClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {}

    @Override
    public void onMessage(String message) {
        messages.add(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {}

    @Override
    public void onError(Exception ex) {
        exceptions.add(ex);
    }
    
}
