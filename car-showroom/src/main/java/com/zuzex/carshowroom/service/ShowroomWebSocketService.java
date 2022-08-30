package com.zuzex.carshowroom.service;

import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface ShowroomWebSocketService {

    List<WebSocketSession> getActiveSessions();
}
