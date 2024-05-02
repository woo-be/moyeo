package com.moyeo.dao;


import com.moyeo.vo.RecruitBoard;

import com.moyeo.vo.RecruitMember;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RecruitMemberDao {

  // 모집 신청하기
  void add(
      @Param("recruitBoardId") int recruitBoardId,
      @Param("memberId") int memberId);

  // 글 작성자를 recruit_member에 추가
  void addWriter(
      @Param("recruitBoardId") int recruitBoardId,
      @Param("memberId") int memberId);

  List<RecruitBoard> findAll(@Param("memberId") int memberId);

  RecruitMember findBy(
      @Param("memberId") int memberId,
      @Param("recruitBoardId") int recruitBoardId);

  List<RecruitMember> findAllApplicant(int recruitBoardId);

  List<RecruitMember> allApplicant(@Param("recruitBoardId") int recruitBoardId);

  int update(RecruitMember recruitMember);

  void delete(
      @Param("recruitBoardId") int recruitBoardId,
      @Param("memberId") int memberId);

  void deleteAll(int recruitBoardId); // recruit_member 테이블의 recruitBoardId가 boardId인 레코드 전부 삭제
}
