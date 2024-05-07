package com.moyeo.service;

import com.moyeo.vo.Member;
import java.sql.Date;
import java.util.List;

public interface MemberService {

  void add(Member member);

  Member get(int no);

  List<Member> list();

  Member get(String email, String password);
  Member get(String email);

  int update(Member member);

  int delete(int no);

  Member get(String phoneNumber, String name, Date birthdate);

  // 비밀번호를 바꾸기 위해 필요한 자료찾기
  Member findBy(String email, String name, String phoneNumber, Date birthdate);

  // mybatis에서는 update,delete,insert 문은 row가 바뀌어 int값으로 들어간다
  // 비밀번호 변경을 위해 member 를 update
  int updatePassword(Member member);

  // 구글 로그인을 하기위해 DB에 같은 email이 저장되어있는지 확인
  Member getByEmail(String email);


}
