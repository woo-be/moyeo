package com.moyeo.dao;

import com.moyeo.vo.RecruitMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RecruitMemberDao {

  // 모집 신청하기
  void add(
      @Param("recruitBoardId") int recruitBoardId,
      @Param("memberId") int memberId);

  void delete(
      @Param("recruitBoardId") int recruitBoardId,
      @Param("memberId") int memberId);

  RecruitMember findBy(
      @Param("memberId") int memberId,
      @Param("recruitBoardId") int recruitBoardId);
}