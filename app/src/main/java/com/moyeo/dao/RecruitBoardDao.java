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
      @Param("rowCount") int rowCount,
      @Param("regionId")int regionId,
      @Param("themeId")int themeId,
      @Param("filter")String filter,
      @Param("keyword")String keyword);

  List<RecruitBoard> findByMemberId(
      @Param("offset") int offset,
      @Param("rowCount") int rowCount,
      @Param("memberId") int memberId);

  List<RecruitBoard> findReqByMemberId(
      @Param("offset") int offset,
      @Param("rowCount") int rowCount,
      @Param("memberId") int memberId);

  List<RecruitBoard> findMyTeamByMemberId(int memberId);

  RecruitBoard findBy(int no);

  int update(RecruitBoard recruitBoard);

  int delete(int no);
  int countAll(
      @Param("regionId") int regionId,
      @Param("themeId") int themeId,
      @Param("filter") String filter,
      @Param("keyword")String keyword);

  int countAllMyPost(@Param("memberId") int memberId);
  int countAllMyReq(@Param("memberId") int memberId);

  // 조회수 증가
  void plusViews(int boardId);

  List<RecruitBoard> findByCurrent(
      @Param("offset") int offset,
      @Param("rowCount") int rowCount,
      @Param("regionId")int regionId,
      @Param("themeId")int themeId,
      @Param("filter")String filter,
      @Param("keyword")String keyword
  );

  List<RecruitBoard> findByCurrentByLimit6();

  RecruitBoard findCurrentAndTotalBy(int recruitBoardId); // 현재인원과 총 모집 인원을 찾음.
}
