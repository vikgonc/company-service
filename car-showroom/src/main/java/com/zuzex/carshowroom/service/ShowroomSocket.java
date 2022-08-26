package com.zuzex.carshowroom.service;

import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface ShowroomSocket {

    List<WebSocketSession> getActiveSessions();
}
