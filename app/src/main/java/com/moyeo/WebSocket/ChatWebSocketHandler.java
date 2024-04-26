package com.moyeo.WebSocket;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

  private static Set<WebSocketSession> sessions = new HashSet<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    sessions.add(session);
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message /* WebSocketMessage<?> message*/)
      throws Exception {
    String url = String.valueOf(session.getUri());
    int index = url.indexOf("recruitBoardId");
    int boardId = Integer.parseInt(url.substring(index+15));

    for (WebSocketSession s : sessions) {
      // recruitId 같이 보내서 여기 팀 사람들한테는 무조건 보내주도록
      // recruitid랑 html의 recruitboardid 같으면 받도록 함
      s.sendMessage(new TextMessage(boardId + ") " + message.getPayload()));
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
