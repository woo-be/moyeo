package com.moyeo.service;

import com.moyeo.vo.RecruitBoard;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RecruitBoardService {
  void add(RecruitBoard board);

  List<RecruitBoard> list(
      // offset -> 몇 번째 row부터 출력할 지, limit-> 한 번에 몇 개 출력할지
      @Param("offset") int offset,
      @Param("rowCount") int rowCount);

  RecruitBoard get(int no);

  int update(RecruitBoard board);

  int delete(int no);

  int countAll();
}
