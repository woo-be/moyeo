package com.moyeo.service;

import com.moyeo.vo.PlanBoard;
import java.awt.Panel;
import java.util.List;

public interface PlanBoardService {

  List<PlanBoard> list(int recruitBoardId);

  PlanBoard get(int planBoardId);

  void add(PlanBoard planBoard);

  int update(PlanBoard planBOard);

}
