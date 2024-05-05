package com.moyeo.service;

import com.moyeo.vo.Alarm;
import com.moyeo.vo.Member;
import java.util.List;

public interface AlarmService {

  List<Alarm> list(int memberId);

  void reviewCommentAdd(Alarm alarm, int reviewBoardId);
  void recruitCommentAdd(Alarm alarm, int reviewBoardId);
  void recruitMemberAdd(Alarm alarm, int reviewBoardId);
  int update(int alarmId);

  boolean getStatus(int alarmId);
  int delete(int alarmId);
  int updateContent(Alarm alarm);
}
