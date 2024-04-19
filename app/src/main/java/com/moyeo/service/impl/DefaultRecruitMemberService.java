package com.moyeo.service.impl;

import com.moyeo.dao.RecruitMemberDao;
import com.moyeo.service.RecruitMemberService;

import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.ReviewBoard;
import com.moyeo.vo.RecruitMember;
import java.util.List;
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


  @Override
  public List<RecruitBoard> list(int memberId) {
    return recruitMemberDao.findAll(memberId);
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

  @Override
  public List<RecruitMember> findAllApplicant(int recruitBoardId) {
    return recruitMemberDao.findAllApplicant(recruitBoardId);
  }
}
