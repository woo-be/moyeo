package com.moyeo.service;

import com.moyeo.vo.Member;
import java.util.List;

public interface MemberService {

  void add(Member member);

  Member get(int no);

  List<Member> list();

  Member get(String email, String password);

  int update(Member member);

  int delete(int no);

  // 페이징처리할때 사용
//  int countAll();

}
