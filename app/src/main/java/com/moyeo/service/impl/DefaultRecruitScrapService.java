package com.moyeo.service.impl;

import com.moyeo.dao.RecruitScrapDao;
import com.moyeo.service.RecruitScrapService;
import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitScrap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
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
  public List<RecruitBoard> list(int pageNo, int pageSize, int memberId) {
    return recruitScrapDao.findAll(pageSize * (pageNo - 1), pageSize, memberId);
  }

  @Override
  public void delete(RecruitScrap recruitScrap) {
    recruitScrapDao.delete(recruitScrap);
  }

  @Override
  public int countAll(@Param("memberId") int memberId){
    return recruitScrapDao.countAll(memberId);
  }
}
