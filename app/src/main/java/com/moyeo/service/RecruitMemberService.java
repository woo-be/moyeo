package com.moyeo.service;

import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitMember;
import java.util.List;

public interface RecruitMemberService {

  void add(int recruitBoardId, int memberId); // 모집 신청하기

  List<RecruitBoard> list(int memberId);
}
