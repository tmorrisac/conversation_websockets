package com.ac.websockets.server;

import javax.enterprise.event.Observes;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(
        value = "/chat/"
)

public class ChatEndpoint {
    private static final Set<Session> CLIENTS = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        Logger.getLogger(ChatEndpoint.class.getName()).log(Level.INFO, "Client connected -- {0}", session.getId());
        CLIENTS.add(session);
    }

    @OnMessage
    public static void broadcast(@Observes @MessageDataQualifer String conversation) {
        for (Session session : CLIENTS) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(conversation, result -> {
                    if (result.isOK()) {
                        Logger.getLogger(ChatEndpoint.class.getName()).log(Level.SEVERE, "sent to client {0} ", session.getId());
                    } else {
                        Logger.getLogger(ChatEndpoint.class.getName()).log(Level.SEVERE, "Could not send to client " + session.getId(),
                                result.getException());
                    }
                });
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        Logger.getLogger(ChatEndpoint.class.getName()).log(Level.INFO, "Client disconnected -- {0}", session.getId());
        CLIENTS.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }
}
