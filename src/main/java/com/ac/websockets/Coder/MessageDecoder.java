package com.ac.websockets.Coder;

import com.ac.websockets.models.Message;
import com.google.gson.Gson;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message> {

    private static Gson gson = new Gson();

    @Override
    public Message decode(String content) {
        return gson.fromJson(content, Message.class);
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
