package com.moyeo.WebSocket;

import java.util.HashSet;
import java.util.Set;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class ChatWebSocketHandler implements WebSocketHandler {

  private static Set<WebSocketSession> sessions = new HashSet<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    sessions.add(session);
  }

  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
      throws Exception {
    for (WebSocketSession s : sessions) {
      s.sendMessage(new TextMessage((String) message.getPayload()));
    }
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    if (session.isOpen()) {
      session.close();
    }
    sessions.remove(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
      throws Exception {
    sessions.remove(session);
  }

  @Override
  public boolean supportsPartialMessages() {
    return false;
  }
}
