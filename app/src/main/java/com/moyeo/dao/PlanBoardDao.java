package com.moyeo.dao;

import com.moyeo.vo.PlanBoard;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlanBoardDao {

  List<PlanBoard> findAll(int recruitBoardId);

  PlanBoard findBy(int planBoardId);

  void add(PlanBoard planBoard);

  int update(PlanBoard planBoard);

  int delete(int planBoardId, int recruitBoardId);

}
