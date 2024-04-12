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

  List<RecruitBoard> findByKeyword(
      @Param("offset") int offset,
      @Param("rowCount") int rowCount,
      @Param("filter") String filter,
      @Param("keyword") String keyword);

  List<RecruitBoard> findAllByRegion(
      @Param("offset") int offset,
      @Param("rowCount") int rowCount,
      @Param("regionId") int regionId);

  // 로그인한 사용자가 즐겨찾기한 게시글 리스트
  List<RecruitBoard> findScrap(int memberId);

  RecruitBoard findBy(int no);

  int update(RecruitBoard recruitBoard);

  int delete(int no);

  int countAll();

  int countAllByRegion(int regionId);

  int countByKeyword(
      @Param("filter") String filter,
      @Param("keyword") String keyword);

  // 조회수 증가
  void plusViews(int boardId);
}
