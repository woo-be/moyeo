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

  List<RecruitBoard> findByMemberId(int memberId);

  List<RecruitBoard> findReqByMemberId(int memberId);

  List<RecruitBoard> findMyTeamByMemberId(int memberId);

  RecruitBoard findBy(int no);

  int update(RecruitBoard recruitBoard);

  int delete(int no);
  int countAll(
      @Param("regionId") int regionId,
      @Param("themeId") int themeId,
      @Param("filter") String filter,
      @Param("keyword")String keyword);

  // 조회수 증가
  void plusViews(int boardId);

  List<RecruitBoard> demoFindAll();
}
