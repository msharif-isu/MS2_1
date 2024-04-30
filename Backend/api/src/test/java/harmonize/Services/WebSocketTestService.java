package harmonize.Services;

import java.io.FileInputStream;
import java.net.URI;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.hc.core5.ssl.SSLContexts;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import lombok.Getter;

public class WebSocketTestService extends WebSocketClient {

    @Getter private List<Exception> exceptions = new ArrayList<>();
    @Getter private List<String> messages = new ArrayList<>();

    public WebSocketTestService(URI serverUri) throws Exception {
        super(serverUri);
        FileInputStream inputStream = new FileInputStream("./target/test-classes/harmonize.crt");
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate serverCertificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
        inputStream.close();

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("harmonize", serverCertificate);

        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(keyStore, null)
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
