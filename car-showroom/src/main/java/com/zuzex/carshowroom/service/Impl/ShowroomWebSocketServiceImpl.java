package com.zuzex.carshowroom.service.Impl;

import com.zuzex.carshowroom.service.ShowroomWebSocketService;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class ShowroomWebSocketServiceImpl extends TextWebSocketHandler implements ShowroomWebSocketService {

    private final List<WebSocketSession> activeSessions;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage("Welcome!"));
        log.info("Created new session");
        activeSessions.add(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        activeSessions.remove(session);
        log.info("Session closed with status: {}", status);
    }
}
