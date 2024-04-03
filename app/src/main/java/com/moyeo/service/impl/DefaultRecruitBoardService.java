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
    recruitBoardDao.add(board);
  }

  @Override
  public List<RecruitBoard> list(int pageNo, int pageSize) {
    return recruitBoardDao.findAll(pageSize * (pageNo - 1), pageSize);
  }

  @Override
  public RecruitBoard get(int no) {
    return recruitBoardDao.findBy(no);
  }

  @Override
  public int update(RecruitBoard board) {
    return recruitBoardDao.update(board);
  }

  @Override
  public int delete(int no) {
    return recruitBoardDao.delete(no);
  }

  @Override
  public int countAll() {
    return recruitBoardDao.countAll();
  }
}
