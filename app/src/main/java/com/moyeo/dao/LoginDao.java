package com.moyeo.dao;

import com.moyeo.vo.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginDao {

  public void add(Member member);

  public int delete(int no);

  public int update(Member member);



}
