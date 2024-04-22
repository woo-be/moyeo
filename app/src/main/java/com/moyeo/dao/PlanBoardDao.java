package com.moyeo.dao;

import com.moyeo.vo.PlanBoard;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlanBoardDao {

  List<PlanBoard> findAll();

  PlanBoard findBy(int planBoardId);



}
