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
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DefaultRecruitScrapService implements RecruitScrapService {

  private static final Log log = LogFactory.getLog(DefaultRecruitBoardService.class);
  private final RecruitScrapDao recruitScrapDao;

  @Transactional
  @Override
  public void add(RecruitScrap recruitScrap) {
    recruitScrapDao.add(recruitScrap);
  }

  @Override
  public List<RecruitBoard> list(int memberId) {
    return recruitScrapDao.findAll(memberId);
  }

  @Transactional
  @Override
  public void delete(RecruitScrap recruitScrap) {
    recruitScrapDao.delete(recruitScrap);
  }

  // 해당 scrap 객체가 테이블상에 존재하면 1, 아니면 0
  @Override
  public int isExist(RecruitScrap recruitScrap) {
    return recruitScrapDao.isExist(recruitScrap);
  }
}
