package com.moyeo.service;

import com.moyeo.vo.RecruitBoard;
import java.util.List;

public interface RecruitBoardService {
  void add(RecruitBoard board);

  List<RecruitBoard> list();

  RecruitBoard get(int no);

  int update(RecruitBoard board);

  int delete(int no);
}
