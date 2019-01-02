package com.ac.websockets.endpoints;

import com.ac.websockets.Coder.MessageDecoder;
import com.ac.websockets.Coder.MessageEncoder;
import com.ac.websockets.models.Message;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(
        value = "/chat",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class
)

public class ChatEndpoint {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    private static HashMap<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session.getId() + " is opened");
        sessions.add(session);
    }

    @OnMessage
    public void handleTextMessage(Session session, Message message) {
        System.out.println("Message from " + session.getId() + ": " + message);
        broadcast(message);
    }

    @OnMessage(maxMessageSize = 2048000)
    public byte[] handleBinaryMessage(byte[] buffer) {
        return buffer;
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println(session.getId() + " is closed");
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    private static void broadcast(Message message) {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendObject(message);
            } catch (IOException | EncodeException ex) {
                ex.printStackTrace();
            }
        }
    }
}
