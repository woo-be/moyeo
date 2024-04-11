package com.moyeo.service.impl;

import com.moyeo.dao.RecruitScrapDao;
import com.moyeo.service.RecruitScrapService;
import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitScrap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultRecruitScrapService implements RecruitScrapService {

  private static final Log log = LogFactory.getLog(DefaultRecruitBoardService.class);
  private final RecruitScrapDao recruitScrapDao;

  @Override
  public void add(RecruitScrap recruitScrap) {
    recruitScrapDao.add(recruitScrap);
  }

  @Override
  public List<RecruitBoard> list(int memberId) {
    return recruitScrapDao.findAll(memberId);
  }
}
