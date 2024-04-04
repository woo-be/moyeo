package com.moyeo.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewLikeDao {

  int deleteAll(int reviewBoardId);
}
