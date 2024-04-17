package com.moyeo.dao;

import com.moyeo.vo.Alarm;
import com.moyeo.vo.Member;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AlarmDao {
  List<Alarm> findAll(@Param("memberId")int memberId);
}
