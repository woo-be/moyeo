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

    List<Alarm> list = new ArrayList<Alarm>();
    list = alarmDao.findAll(memberId);
    for (Alarm alarm : list) {
      if (!alarm.isChecked()) {
        alarm.setContent(alarm.getContent()+
            alarm.getAlarmId()+
            "' target='_blank'>[이동]</td></a><td>x</td>");
      } else if (alarm.isChecked()) {
        alarm.setContent(alarm.getContent() +
            alarm.getAlarmId()+
            "' target='_blank'>[이동]</td></a><td>o</td>");
      }
    }
    return list;
  }

  @Override
  public void add(Alarm alarm) {
    alarmDao.add(alarm);
  }

  @Override
  public int update(int alarmId) {
    return alarmDao.statusUpdate(alarmId);
  }

  @Override
  public boolean getStatus(int alarmId) {
    return alarmDao.getStatus(alarmId);
  }
}
