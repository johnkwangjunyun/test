package com.example.demo.chat.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {
    private final String roomId;
    private final String name;
    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void sendMessage(TextMessage message) {
        this.getSessions()
                .parallelStream()
                .forEach(session -> sendMessageToSession(session, message));
    }

    private void sendMessageToSession(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void join(WebSocketSession session) {
        sessions.add(session);
    }

    public static ChatRoom of(String roomId, String name) {
        return ChatRoom.builder()
                .roomId(roomId)
                .name(name)
                .build();
    }
}
