package com.moyeo.dao;

import com.moyeo.vo.RecruitBoard;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecruitBoardDao {
  void add(RecruitBoard recruitBoard);
  List<RecruitBoard> findAll();
  RecruitBoard findBy(int no);
  int update(RecruitBoard recruitBoard);
  int delete(int no);
}
