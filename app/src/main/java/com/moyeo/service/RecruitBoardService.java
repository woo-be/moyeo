package com.moyeo.service;

import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitComment;
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
      @Param("filter")String filter,
      @Param("keyword")String keyword);

  // 멤버 목록에 내가 존재하는 리스트
  List<RecruitBoard> list(int memberId);

  RecruitBoard get(int boardId);

  int update(RecruitBoard board);

  int delete(int boardId);

  int countAll(@Param("regionId") int regionId,
      @Param("filter")String filter,
      @Param("keyword")String keyword);

  List<RecruitPhoto> getRecruitPhotos(int no);

  RecruitPhoto getRecruitPhoto(int fileNo);

  int deleteRecruitPhoto(int fileNo);

  RecruitComment getComment(int commentId);

  void addComment(RecruitComment comment);

  int updateComment(RecruitComment recruitComment);

  int deleteComment(int commentId);

  // 조회수 증가
  void plusViews(int boardId);
}
