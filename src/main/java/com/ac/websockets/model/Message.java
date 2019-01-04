package com.ac.websockets.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private String from;
    private String content;

    @Override
    public String toString() {
        return super.toString();
    }
}
