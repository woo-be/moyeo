package com.moyeo.dao;

import com.moyeo.vo.PlanBoard;
import java.sql.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlanBoardDao {

  List<PlanBoard> findAll(int recruitBoardId);

  PlanBoard findBy(int planBoardId);

  void add(PlanBoard planBoard);

  int update(PlanBoard planBoard);

  int delete(int planBoardId, int recruitBoardId);

  List<PlanBoard> findByTripDate(@Param("tripDate")String tripDate, @Param("recruitBoardId")int recruitBoardId);

}
