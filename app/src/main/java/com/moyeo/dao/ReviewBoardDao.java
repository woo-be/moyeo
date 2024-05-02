package com.moyeo.dao;

import com.moyeo.vo.ReviewBoard;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewBoardDao {

  void add(ReviewBoard reviewBoard);

  void increaseViews(int reviewBoardId);

  List<ReviewBoard> reviewList(@Param("memberId") int memberId, @Param("pageSize") int pageSize,
      @Param("offset") int offset);

  List<ReviewBoard> scrapList(@Param("memberId") int memberId, @Param("pageSize") int pageSize,
      @Param("offset") int offset);

  List<ReviewBoard> findAll(@Param("offset") int offset, @Param("rowCount") int rowCount,
      @Param("regionId")int regionId, @Param("themeId")int themeId, @Param("filter")String filter, @Param("keyword")String keyword);

  ReviewBoard findBy(@Param("reviewBoardId") int reviewBoardId);

  int delete(int reviewBoardId);

  int countAll(@Param("regionId")int regionId, @Param("themeId")int themeId, @Param("filter")String filter, @Param("keyword")String keyword);


  int countPostedByMember(@Param("memberId") int memberId);

  int update(ReviewBoard reviewBoard);

  ReviewBoard get(int id);

  int countScrapByMember(int memberId);

  //여행후기 최신게시글
  List<ReviewBoard> findByCreatedDate(@Param("offset") int offset, @Param("rowCount") int rowCount,
      @Param("regionId") int regionId, @Param("themeId") int themeId, @Param("filter")String filter, @Param("keyword")String keyword);

  List<ReviewBoard> findByCreatedDateByLimit3();

  List<ReviewBoard> findByLikeCountByLimit3();

  List<ReviewBoard> findByLikeCount(@Param("offset") int offset, @Param("rowCount") int rowCount,
      @Param("regionId")int regionId, @Param("themeId")int themeId, @Param("filter")String filter, @Param("keyword")String keyword);

  List<ReviewBoard> findByViewsByLimit3();

  List<ReviewBoard> findByViews(@Param("offset") int offset, @Param("rowCount") int rowCount,
      @Param("regionId")int regionId, @Param("themeId")int themeId, @Param("filter")String filter, @Param("keyword")String keyword);


}
