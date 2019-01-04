package com.ac.websockets.client;

import com.ac.websockets.decoder.ConversationDecoder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
public class ConversationWebSocketClientSession {
    Session session = null;

    @PostConstruct
    public void init(){
        try {
            WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
            List<Class<? extends Decoder>> decoders = new ArrayList<>();
            decoders.add(ConversationDecoder.class);
            session = webSocketContainer.connectToServer(new ConversationWebSocketClient(),
                    ClientEndpointConfig.Builder.create().decoders(decoders).build(),
                    URI.create("ws://localhost:8080/conversation/"));
        } catch (DeploymentException | IOException ex) {
            Logger.getLogger(ConversationWebSocketClientSession.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @PreDestroy
    public void close(){
        try {
            session.close();
            System.out.println("WebSocket Session closed");
        } catch (IOException ex) {
            Logger.getLogger(ConversationWebSocketClientSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
