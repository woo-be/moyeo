package com.moyeo.service;

import com.moyeo.vo.Alarm;
import com.moyeo.vo.Member;
import java.util.List;

public interface AlarmService {

  List<Alarm> list(int memberId);

  void add(Alarm alarm);
}
