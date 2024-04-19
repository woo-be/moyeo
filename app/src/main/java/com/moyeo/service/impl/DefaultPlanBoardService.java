package com.moyeo.service.impl;

import com.moyeo.dao.PlanBoardDao;
import com.moyeo.service.PlanBoardService;
import com.moyeo.vo.PlanBoard;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class DefaultPlanBoardService implements PlanBoardService {
  private final PlanBoardDao planBoardDao;
  @Override
  public List<PlanBoard> list(int recruitBoardId) {
    return planBoardDao.findAll(recruitBoardId);
  }

  @Override
  public PlanBoard get(int planBoardId) {
    PlanBoard planBoard = planBoardDao.findBy(planBoardId);
    return planBoard;
  }

  @Transactional
  @Override
  public void add(PlanBoard planBoard) {
    planBoardDao.add(planBoard);
  }

  @Transactional
  @Override
  public int update(PlanBoard planBOard) {
    int count = planBoardDao.update(planBOard);
    return count;
  }
}
