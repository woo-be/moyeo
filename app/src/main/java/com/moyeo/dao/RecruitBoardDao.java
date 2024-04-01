package com.moyeo.dao;

import com.moyeo.vo.ReviewBoard;
import java.util.List;

public interface RecruitBoardDao {
  void add(ReviewBoard reviewBoard);
  List<ReviewBoard> findAll();
  ReviewBoard findBy(int no);
  int update(ReviewBoard reviewBoard);
  int delete(int no);
}
