package com.ac.websockets.decoder;

import com.ac.websockets.model.Conversation;
import com.google.gson.Gson;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class ConversationDecoder implements Decoder.Text<Conversation> {

    private static Gson gson = new Gson();

    @Override
    public Conversation decode(String content) {
        return gson.fromJson(content, Conversation.class);
    }

    @Override
    public boolean willDecode(String content) {
        return (content != null);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }
}
