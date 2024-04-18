package com.moyeo.controller;

import com.moyeo.service.RecruitBoardService;
import com.moyeo.vo.Member;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("myplan")
public class MypagePlanController {

  private final RecruitBoardService recruitBoardService;

  @GetMapping("list")
  public void list(Model model, HttpSession session) throws Exception {

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요한 서비스입니다.");
    }

    model.addAttribute("list", recruitBoardService.teamlist(loginUser.getMemberId()));
  }

}
