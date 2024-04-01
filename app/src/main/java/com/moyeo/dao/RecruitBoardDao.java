package com.moyeo.dao;

import com.moyeo.vo.RecruitBoard;
import java.util.List;

public interface RecruitBoardDao {

  void add(RecruitBoard reviewBoard);

  List<RecruitBoard> findAll();

  RecruitBoard findBy(int no);

  int update(RecruitBoard recruitBoard);

  int delete(int no);
}
