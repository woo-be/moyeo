package com.moyeo.dao;

import com.moyeo.vo.ReviewComment;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewCommentDao {
  List<ReviewComment> findAllByReviewId(@Param("reviewBoardId")int reviewBoardId);
  void reviewCommentPost(ReviewComment reviewComment);
}
