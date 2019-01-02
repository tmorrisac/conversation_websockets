package com.ac.websockets.models;

import lombok.Data;

@Data
public class Message {
    private String from;
    private String to;
    private String content;

    @Override
    public String toString() {
        return super.toString();
    }
}
