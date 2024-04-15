package com.moyeo.dao;

import com.moyeo.vo.ReviewBoard;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewBoardDao {

  void add(ReviewBoard reviewBoard);

  void increaseViews(int reviewBoardId);

  List<ReviewBoard> reviewList(@Param("memberId") int memberId, @Param("pageSize")int pageSize, @Param("offset")int offset);

  List<ReviewBoard> findAllNoPaging ();
  List<ReviewBoard> findAll(@Param("offset") int offset, @Param("rowCount") int rowCount);

  List<ReviewBoard> findAllByRegionId(@Param("offset") int offset, @Param("rowCount") int rowCount,
      @Param("regionId") int regionId);

  ReviewBoard findBy(@Param("reviewBoardId") int reviewBoardId);

  int delete(int reviewBoardId);

  int countAll();

  int countAllByRegionId(@Param("regionId") int regionId);
  int countPostedByMember(@Param("memberId") int memberId);

  int update(ReviewBoard reviewBoard);

  ReviewBoard get(int id);
}
