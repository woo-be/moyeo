package com.moyeo.dao;

import com.moyeo.vo.ReviewBoard;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewBoardDao {

  void add(ReviewBoard reviewBoard);

  List<ReviewBoard> findAll(@Param("offset")int offset, @Param("rowCount")int rowCount);
  List<ReviewBoard> findAllByRegionId(@Param("offset")int offset, @Param("rowCount")int rowCount, @Param("regionId") int regionId);
  
  ReviewBoard findBy(@Param("reviewBoardId")int reviewBoardId);

  int delete(int reviewBoardId);

  int countAll();

  int countAllByRegionId(@Param("regionId")int regionId);

  int update(ReviewBoard reviewBoard);

  ReviewBoard get(int id);
}
