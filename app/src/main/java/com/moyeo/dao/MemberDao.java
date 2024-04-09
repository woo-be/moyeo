package com.moyeo.dao;

import com.moyeo.vo.Member;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberDao {

  // 회원가입 데이터 넣을때
  void add(Member member);

  // memberid 찾을때
  int delete(int no);

  // xml에서 select list 받을때
  List<Member> findAll();

  // 정보 업데이트
  int update(Member member);

  // member_id 찾을때
  Member findBy(int no);

  // 로그인 할때
  Member findByEmailAndPassword(
      @Param("email") String email,
      @Param("password") String password);

}
