package com.moyeo.service;

import com.moyeo.vo.Member;
import java.sql.Date;
import java.util.List;

public interface MemberService {

  void add(Member member);

  Member get(int no);

  List<Member> list();

  Member get(String email, String password);

  int update(Member member);

  int delete(int no);

  Member get(String phoneNumber, String name, Date birthdate);


}
