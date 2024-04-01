package com.moyeo.service.impl;

import com.moyeo.dao.RecruitBoardDao;
import com.moyeo.service.RecruitBoardService;
import com.moyeo.vo.RecruitBoard;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultRecruitBoardService implements RecruitBoardService {

  private final RecruitBoardDao recruitBoardDao;

  @Override
  public void add(RecruitBoard board) {

  }

  @Override
  public List<RecruitBoard> list() {
    return recruitBoardDao.findAll();
  }

  @Override
  public RecruitBoard get(int no) {
    return null;
  }

  @Override
  public int update(RecruitBoard board) {
    return 0;
  }

  @Override
  public int delete(int no) {
    return 0;
  }
}
