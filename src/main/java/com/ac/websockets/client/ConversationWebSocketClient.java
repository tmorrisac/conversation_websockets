package com.ac.websockets.client;

import redis.clients.jedis.Jedis;

import javax.websocket.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ConversationWebSocketClient extends Endpoint {
    private Jedis jedis;
    private static String CONVERSATION_REDIS_KEY = "conversations";
    private static Set<String> CONVERSATIONS_IN_REDIS = Collections.synchronizedSet(new HashSet<String>());

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("Server session established");
        jedis = new Jedis();
        jedis.zadd(CONVERSATION_REDIS_KEY, 1, "Connected!");
        session.addMessageHandler((MessageHandler.Whole<String>) message -> jedis.zincrby(CONVERSATION_REDIS_KEY, 1, message));
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        jedis.close();
        System.out.println("Redis connection closed");
    }
}
