package com.moyeo.service.impl;

import com.moyeo.dao.PlanBoardDao;
import com.moyeo.service.PlanBoardService;
import com.moyeo.vo.PlanBoard;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultPlanBoardService implements PlanBoardService {
  private final PlanBoardDao planBoardDao;
  @Override
  public List<PlanBoard> list() {
    return planBoardDao.findAll();
  }
}
