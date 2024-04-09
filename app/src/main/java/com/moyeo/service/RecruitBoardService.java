package com.moyeo.service;

import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitComment;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RecruitBoardService {
  void add(RecruitBoard board);

  List<RecruitBoard> list(
      // offset -> 몇 번째 row부터 출력할 지, limit-> 한 번에 몇 개 출력할지
      @Param("offset") int offset,
      @Param("rowCount") int rowCount);

  // 로그인한 사용자가 즐겨찾기한 게시글 리스트
  List<RecruitBoard> scrapList(int memberId);

  RecruitBoard get(int boardId);

  int update(RecruitBoard board);

  int delete(int boardId);

  int countAll();

  RecruitComment getComment(int commentId);

  void addComment(RecruitComment comment);

  int updateComment(RecruitComment recruitComment);

  int deleteComment(int commentId);

  // 조회수 증가
  void plusViews(int boardId);
}
