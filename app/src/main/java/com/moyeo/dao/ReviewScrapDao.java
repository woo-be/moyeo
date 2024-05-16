package com.moyeo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewScrapDao {

  void add (@Param("memberId")int memberId, @Param("reviewBoardId")int reviewBoardId);

  int deleteAll(int reviewBoardId);
  int checked(@Param("memberId")int memberId, @Param("reviewBoardId")int reviewBoardId);

  int delete(@Param("memberId")int memberId, @Param("reviewBoardId")int reviewBoardId);

}
