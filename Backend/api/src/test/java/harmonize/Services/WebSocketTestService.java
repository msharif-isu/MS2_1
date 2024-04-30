package harmonize.Services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import lombok.Getter;

public class WebSocketTestService extends WebSocketClient {

    @Getter private List<Exception> exceptions = new ArrayList<>();
    @Getter private List<String> messages = new ArrayList<>();

    public WebSocketTestService(URI serverUri) throws Exception {
        super(serverUri);
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(null, new TrustAllStrategy())
                .build();
        setSocketFactory(sslcontext.getSocketFactory());
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
