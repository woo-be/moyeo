package com.moyeo.service;


import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitScrap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RecruitScrapService {
  void add(RecruitScrap recruitScrap);

  List<RecruitBoard> list(
      @Param("offset") int offset,
      @Param("rowCount") int rowCount,
      @Param("memberId") int memberId);

  void delete(RecruitScrap recruitScrap);

  int countAll(@Param("memberId") int memberId);
}
