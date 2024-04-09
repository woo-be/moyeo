package com.moyeo.dao;

import com.moyeo.vo.RecruitBoard;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

@Mapper
public interface RecruitBoardDao {
  @Transactional
  void add(RecruitBoard recruitBoard);
  List<RecruitBoard> findAll(
      @Param("offset") int offset,
      @Param("rowCount") int rowCount);
  RecruitBoard findBy(int no);
  @Transactional
  int update(RecruitBoard recruitBoard);
  @Transactional
  int delete(int no);
  int countAll();
  void plusViews(int boardId);
}
