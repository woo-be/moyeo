package com.moyeo.controller;

import com.moyeo.service.RecruitMemberService;
import com.moyeo.vo.Member;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/recruitMember")
public class RecruitMemberController {

  private static final Log log = LogFactory.getLog(RecruitMemberController.class);
  private final RecruitMemberService recruitMemberService;

  @GetMapping("add")
  public String add(int recruitBoardId, HttpSession session) throws Exception { // 모집 신청하기

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요한 서비스입니다.");
    }

    recruitMemberService.add(recruitBoardId, loginUser.getMemberId());

    return "redirect:/recruit/view?recruitBoardId=" + recruitBoardId;
  }

  @GetMapping("delete")
  public String delete(int recruitBoardId, HttpSession session) throws Exception{ // 모집 신청 취소하기

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요한 서비스입니다.");
    }

    recruitMemberService.delete(recruitBoardId, loginUser.getMemberId());

    return "redirect:/recruit/view?recruitBoardId=" + recruitBoardId;
  }

}
