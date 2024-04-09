package com.moyeo.service;

import com.moyeo.vo.ReviewBoard;
import java.util.List;

public interface ReviewBoardService {

  void add(ReviewBoard reviewBoard);
  void increaseViews(int reviewBoardId);

  List<ReviewBoard> list(int pageNo, int pageSize);
  List<ReviewBoard> list(int pageNo, int pageSize, int regionId);
  ReviewBoard get(int reviewBoardId);

  int delete(int reviewBoardId);
  int countAll();
  int countAll(int regionId);
  int update(ReviewBoard reviewBoard);
}
