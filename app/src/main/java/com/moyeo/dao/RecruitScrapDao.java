package com.moyeo.dao;

import com.moyeo.vo.RecruitScrap;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecruitScrapDao {

  void add(RecruitScrap recruitScrap);
}
