package com.moyeo.service.impl;

import com.moyeo.dao.MessageDao;
import com.moyeo.service.MessageService;
import com.moyeo.vo.Msg;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultMessageService implements MessageService {

  private final MessageDao messageDao;

  @Override
  public void add(Msg msg) {
    msg.setMsg(msg.getMsg());
    messageDao.add(msg);
  }

  @Override
  public List<Msg> list(int recruitBoardId) {
    return messageDao.list(recruitBoardId);
  }
}
