package com.moyeo.dao;

import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitScrap;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecruitScrapDao {

  void add(RecruitScrap recruitScrap);

  List<RecruitBoard> findAll(int memberId);

  void delete(RecruitScrap recruitScrap);
}
