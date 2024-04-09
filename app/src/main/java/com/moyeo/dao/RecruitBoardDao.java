package com.moyeo.dao;

import com.moyeo.vo.RecruitBoard;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RecruitBoardDao {

  void add(RecruitBoard recruitBoard);
  List<RecruitBoard> findAll(
      @Param("offset") int offset,
      @Param("rowCount") int rowCount);
  RecruitBoard findBy(int no);

  int update(RecruitBoard recruitBoard);

  int delete(int no);
  int countAll();
  // 조회수 증가
  void plusViews(int boardId);
}
