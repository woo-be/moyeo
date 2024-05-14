package com.moyeo.service;

import com.moyeo.vo.ReviewBoard;
import com.moyeo.vo.ReviewPhoto;
import com.moyeo.vo.ReviewComment;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReviewBoardService {

  void add(ReviewBoard reviewBoard);

  void increaseViews(int reviewBoardId);

  List<ReviewBoard> reviewList(int memberId, int pageSize, int pageNo);

  List<ReviewBoard> list(int pageNo, int pageSize, int regionId,int themeId, String filter, String keyword);

  ReviewBoard get(int reviewBoardId);

  int delete(int reviewBoardId);

  int countAll(int regionId, int themeId, String filter, String keyword);


  int countPostedByMember(int memberId);

  int update(ReviewBoard reviewBoard);

  List<ReviewPhoto> getReviewPhotos(int reviewBoardId);

  ReviewPhoto getReviewPhoto(int reviewPhotoId);

  int deleteReviewPhoto(int reviewPhotoId);

  List<ReviewBoard> scrapList(int memberId, int pageNo, int pageSize);

  int countScrapByMember(@Param("memberId") int memberId);

  List<ReviewBoard> findByCreatedDate(int pageNo, int pageSize, int regionId,int themeId, String filter, String keyword);

  List<ReviewBoard> findByCreatedDateByLimit3();

  List<ReviewBoard> findByLikeCountByLimit3();

  List<ReviewBoard> findByLikeCount(int pageNo, int pageSize, int regionId,int themeId, String filter, String keyword);

  List<ReviewBoard> findByViewsByLimit3();

  List<ReviewBoard> findByViews(int pageNo, int pageSize, int regionId,int themeId, String filter, String keyword);

  ReviewComment getComment(int commentId);

  void addComment(ReviewComment comment);

  int updateComment(ReviewComment reviewComment);

  int deleteComment(int commentId);

  List<ReviewBoard> findByCreatedDate2();
}
