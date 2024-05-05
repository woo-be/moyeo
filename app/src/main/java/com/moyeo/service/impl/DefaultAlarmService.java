package com.moyeo.service.impl;

import com.moyeo.dao.AlarmDao;
import com.moyeo.service.AlarmService;
import com.moyeo.vo.Alarm;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultAlarmService implements AlarmService {

  private final AlarmDao alarmDao;
  private final Log log = LogFactory.getLog(DefaultAlarmService.class);

  @Override
  public List<Alarm> list(int memberId) {
    return alarmDao.findAll(memberId);
  }

  @Override
  public void reviewCommentAdd(Alarm alarm, int reviewBoardId) {
    alarmDao.add(alarm);
    alarm.setContent(
        alarm.getContent()+
            "&alarmId="+
            alarm.getAlarmId()+
            "\">"+
            reviewBoardId+
            "번 후기</a>에 댓글을 등록했습니다."
    );
    alarmDao.updateContent(alarm);
  }

  @Override
  public void recruitCommentAdd(Alarm alarm, int recruitBoardId) {
    alarmDao.add(alarm);
    alarm.setContent(
        alarm.getContent() +
            "&alarmId=" +
            alarm.getAlarmId() +
            "\">" +
            recruitBoardId +
            "번 모집</a>에 댓글을 등록했습니다."
    );
    alarmDao.updateContent(alarm);
  }

  @Override
  public void recruitMemberAdd(Alarm alarm, int recruitBoardId) {
    alarmDao.add(alarm);
    alarm.setContent(
        alarm.getContent()+
            "&alarmId="+
            alarm.getAlarmId()+
            "\">"+
            recruitBoardId+
            "번 모집</a>에 신청했습니다."
    );
    alarmDao.updateContent(alarm);
  }

  @Override
  public int update(int alarmId) {
    return alarmDao.statusUpdate(alarmId);
  }

  @Override
  public boolean getStatus(int alarmId) {
    return alarmDao.getStatus(alarmId);
  }

  @Override
  public int delete(int alarmId) {
    return alarmDao.delete(alarmId);
  }

  public int updateContent(Alarm alarm){
    return alarmDao.updateContent(alarm);
  }
}
