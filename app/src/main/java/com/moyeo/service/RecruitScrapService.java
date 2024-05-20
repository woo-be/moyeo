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

  int isExist(RecruitScrap recruitScrap); // 해당 scrap 객체가 테이블상에 존재하면 1, 아니면 0

  int countAll(@Param("memberId") int memberId);

  int deleteAll(int[] recruitBoardIdList, int memberId);
}
