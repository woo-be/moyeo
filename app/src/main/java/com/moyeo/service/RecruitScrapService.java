package com.moyeo.service;


import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitScrap;
import java.util.List;

public interface RecruitScrapService {
  void add(RecruitScrap recruitScrap);

  List<RecruitBoard> list(int memberId);

  void delete(RecruitScrap recruitScrap);
}
