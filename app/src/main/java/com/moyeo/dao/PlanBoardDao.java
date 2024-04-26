package com.moyeo.dao;

import com.moyeo.vo.Pin;
import com.moyeo.vo.PlanBoard;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlanBoardDao {

  List<PlanBoard> findAll(int recruitBoardId);

  PlanBoard findBy(int planBoardId);

  PlanBoard findByPlanBoard(@Param("recruitBoardId") int recruitBoardId, @Param("tripDate") String tripDate, @Param("latitude") double latitude, @Param("longitude") double longitude);

  void add(PlanBoard planBoard);

  int update(PlanBoard planBoard);

  int delete(int planBoardId, int recruitBoardId);

  List<Pin> findByPin(@Param("recruitBoardId") int recruitBoardId, @Param("tripDate") String tripDate);


}
