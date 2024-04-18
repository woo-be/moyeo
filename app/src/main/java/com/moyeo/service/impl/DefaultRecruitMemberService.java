package com.moyeo.service.impl;

import com.moyeo.dao.RecruitMemberDao;
import com.moyeo.service.RecruitMemberService;
import com.moyeo.vo.RecruitMember;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DefaultRecruitMemberService implements RecruitMemberService {

  private static final Log log = LogFactory.getLog(DefaultRecruitMemberService.class);
  private final RecruitMemberDao recruitMemberDao;

  @Transactional
  @Override
  public void add(int recruitBoardId, int memberId) { // 모집 신청하기
    recruitMemberDao.add(recruitBoardId, memberId);
  }

  @Transactional
  @Override
  public void delete(int recruitBoardId, int memberId) { // 모집 신청 취소하기
    recruitMemberDao.delete(recruitBoardId, memberId);
  }

  @Override
  public RecruitMember findBy(int memberId, int recruitBoardId) {
    return recruitMemberDao.findBy(memberId, recruitBoardId);
  }
}
