package com.moyeo.dao;

import com.moyeo.vo.ReviewPhoto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewPhotoDao {

  void add(ReviewPhoto photo);

  int addAll(List<ReviewPhoto> photos);

  int deleteAll(int reviewBoardId);

  int delete(int reviewPhotoId);

  List<ReviewPhoto> findAllByReviewBoardId(int reviewBoardId);

  ReviewPhoto findByReviewPhotoId(int reviewPhotoId);

}
