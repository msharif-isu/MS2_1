package harmonize.Controllers;

import org.springframework.stereotype.Component;

import jakarta.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint(value = "/feed")
public class FeedController {
    
}
