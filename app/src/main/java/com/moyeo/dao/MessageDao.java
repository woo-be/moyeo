package com.moyeo.dao;

import com.moyeo.vo.Msg;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MessageDao {
  void add(Msg msg);
  List<Msg> list(int recruitBoardId);
}
