package com.moyeo.service.impl;

import com.moyeo.dao.AlarmDao;
import com.moyeo.service.AlarmService;
import com.moyeo.vo.Alarm;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultAlarmService implements AlarmService {
  private final AlarmDao alarmDao;
  @Override
  public List<Alarm> list(int memberId) {
    return alarmDao.findAll(memberId);
  }

  @Override
  public void add(Alarm alarm) {
    alarmDao.add(alarm);
  }
}
