package com.ac.websockets.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Conversation {
    private List<Message> messages;
}
