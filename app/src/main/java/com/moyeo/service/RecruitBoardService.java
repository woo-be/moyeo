package com.moyeo.service;

import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitComment;
import com.moyeo.vo.RecruitMember;
import com.moyeo.vo.RecruitPhoto;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RecruitBoardService {
  void add(RecruitBoard board);

  List<RecruitBoard> list(
      // offset -> 몇 번째 row부터 출력할 지, limit-> 한 번에 몇 개 출력할지
      @Param("offset") int offset,
      @Param("rowCount") int rowCount,
      @Param("regionId")int regionId,
      @Param("themeId") int themeId,
      @Param("filter")String filter,
      @Param("keyword")String keyword);

  List<RecruitBoard> mypost(  // 내가 생성한 모집
      @Param("offset") int offset,
      @Param("rowCount") int rowCount,
      @Param("memberId") int memberId);

  List<RecruitBoard> myrequest( // 내가 신청한 모집
      @Param("offset") int offset,
      @Param("rowCount") int rowCount,
      @Param("memberId") int memberId);

  List<RecruitBoard> teamlist(int memberId);  // 멤버 목록에 내가 존재하는 모집글

  RecruitBoard get(int boardId);

  int update(RecruitBoard board);

  int delete(int boardId);

  int countAll(
      @Param("regionId") int regionId,
      @Param("themeId") int themeId,
      @Param("filter")String filter,
      @Param("keyword")String keyword);

  int countAllMyPost(@Param("memberId") int memberId);

  int countAllMyReq(@Param("memberId") int memberId);

  List<RecruitPhoto> getRecruitPhotos(int no);

  RecruitPhoto getRecruitPhoto(int fileNo);

  int deleteRecruitPhoto(int fileNo);

  RecruitComment getComment(int commentId);

  void addComment(RecruitComment comment);

  int updateComment(RecruitComment recruitComment);

  int deleteComment(int commentId);

  // 조회수 증가
  void plusViews(int boardId);

  List<RecruitBoard> findByCurrent(
      @Param("offset") int offset,
      @Param("rowCount") int rowCount,
      @Param("regionId")int regionId,
      @Param("themeId") int themeId,
      @Param("filter")String filter,
      @Param("keyword")String keyword);

  List<RecruitBoard> findByCurrentByLimit6();

  RecruitBoard findCurrentAndTotalBy(int recruitBoardId);
}
