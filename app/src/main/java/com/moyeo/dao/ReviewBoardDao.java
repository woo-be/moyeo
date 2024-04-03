package com.moyeo.dao;

import com.moyeo.vo.ReviewBoard;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewBoardDao {

  void add(ReviewBoard reviewBoard);

  List<ReviewBoard> findAll(@Param("offset")int offset, @Param("rowCount")int rowCount);
  
  ReviewBoard findBy(@Param("reviewBoardId")int reviewBoardId);

  int delete(int reviewBoardId);

  int countAll();
}
