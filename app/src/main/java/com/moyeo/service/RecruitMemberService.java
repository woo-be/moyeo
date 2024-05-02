package com.moyeo.service;


import com.moyeo.vo.RecruitBoard;

import com.moyeo.vo.RecruitMember;
import java.util.List;

public interface RecruitMemberService {

  void add(int recruitBoardId, int memberId); // 모집 신청하기

  void addWriter(int recruitBoardId, int memberId); // 글 작성자를 recruit_member에 추가

  List<RecruitBoard> list(int memberId);

  void delete(int recruitBoardId, int memberId); // 모집 신청 취소하기

  RecruitMember findBy(int memberId, int recruitBoardId);

  List<RecruitMember> findAllApplicant(int recruitBoardId);
  List<RecruitMember> allApplicant(int recruitBoardId);

  int update(RecruitMember recruitMember);

}
