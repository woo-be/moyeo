package com.moyeo.service;

import com.moyeo.vo.Msg;
import java.util.List;

public interface MessageService {
  void add(Msg msg);

  List<Msg> list(int recruitBoardId);

}
