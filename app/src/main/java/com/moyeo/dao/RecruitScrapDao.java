package com.moyeo.dao;

import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitScrap;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RecruitScrapDao {

  void add(RecruitScrap recruitScrap);

  List<RecruitBoard> findAll(
      @Param("offset") int offset,
      @Param("rowCount") int rowCount,
      @Param("memberId") int memberId);

  void delete(RecruitScrap recruitScrap);

  int isExist(RecruitScrap recruitScrap); // 해당 scrap 객체가 테이블상에 존재하면 1, 아니면 0

  int countAll(int memberId);
}
