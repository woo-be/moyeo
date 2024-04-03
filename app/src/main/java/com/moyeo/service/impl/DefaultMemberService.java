package com.moyeo.service.impl;

import com.moyeo.dao.MemberDao;
import com.moyeo.service.MemberService;
import com.moyeo.vo.Member;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultMemberService implements MemberService {

  private static final Log log = LogFactory.getLog(DefaultMemberService.class);

  private final MemberDao memberDao;


  @Override
  public void add(Member member) {
    memberDao.add(member);
  }


}
