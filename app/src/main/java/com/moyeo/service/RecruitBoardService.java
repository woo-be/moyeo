package com.moyeo.service;

import com.moyeo.vo.ReviewBoard;
import java.util.List;

public interface RecruitBoardService {
// add list get update delete
  void add(ReviewBoard board);

  List<ReviewBoard> list();

  ReviewBoard get(int no);

  int update(ReviewBoard board);

  int delete(int no);
}
