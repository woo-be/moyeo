package com.moyeo.WebSocket;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@RequiredArgsConstructor
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

  private final static Log log = LogFactory.getLog(ChatWebSocketHandler.class);
  private static Set<WebSocketSession> sessions;
  private final Map<Integer, Set<WebSocketSession>> teamSessions = new HashMap<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    // 채팅방에 입장한 recruitBoardId를 식별
    String url = String.valueOf(session.getUri());
    int index = url.indexOf("recruitBoardId");
    int chatRoomId = Integer.parseInt(url.substring(index + 15));

    // HashMap에서 채팅방에 해당하는 번호 HashSet을 꺼냈을 때 없으면은 HashSet을 새로 만들고 HashMap에 저장한다.
    sessions = teamSessions.get(chatRoomId);
    if (sessions == null) {
      sessions = new HashSet<>();
      teamSessions.put(chatRoomId, sessions);
      log.debug("팀 세션들(생성): " + sessions);
    }

    // 채팅방에 해당하는 팀의 세션을 더한다.
    sessions.add(session);
    log.debug("팀 세션들(기존): " + sessions);
  }

  @Override
  public void handleTextMessage(WebSocketSession session,
      TextMessage message /* WebSocketMessage<?> message*/)
      throws Exception {

    String url = String.valueOf(session.getUri());
    int index = url.indexOf("recruitBoardId");
    int chatRoomId = Integer.parseInt(url.substring(index + 15));

    // HashMap에서 팀세션을 꺼내서 메세지를 보낸다.
    sessions = teamSessions.get(chatRoomId);
    for (WebSocketSession s : sessions) {
      // recruitId 같이 보내서 여기 팀 사람들한테는 무조건 보내주도록
      // recruitid랑 html의 recruitboardid 같으면 받도록 함
      s.sendMessage(new TextMessage(chatRoomId + ") " + message.getPayload()));
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
