package com.moyeo.controller;

import com.moyeo.service.RecruitBoardService;
import com.moyeo.service.RecruitMemberService;
import com.moyeo.vo.ErrorName;
import com.moyeo.vo.Member;
import com.moyeo.vo.MoyeoError;
import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitMember;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("myrecruit")
public class MypageRecruitController {

  private final static Log log = LogFactory.getLog(MypageRecruitController.class);
  private final RecruitBoardService recruitBoardService;
  private final RecruitMemberService recruitMemberService;

  @GetMapping("posted")
  public void mypost(Model model, HttpSession session) throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }
    model.addAttribute("mypost", recruitBoardService.mypost(loginUser.getMemberId()));
  }

  @GetMapping("/appl")
  public void appllist(Model model, int recruitBoardId, HttpSession session) throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }

    RecruitBoard recruitBoard = recruitBoardService.get(recruitBoardId);
    if (recruitBoard.getWriter().getMemberId() != loginUser.getMemberId()) {
      throw new MoyeoError(ErrorName.ACCESS_DENIED, "/myrecruit/posted");
    }

    model.addAttribute("recruitBoardId", recruitBoardId);
    model.addAttribute("applicants", recruitMemberService.findAllApplicant(recruitBoardId));
  }

  @PostMapping("/appl")
  public String applStatusUpdate(RecruitMember recruitMember, HttpSession session) throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }

    RecruitBoard recruitBoard = recruitBoardService.get(recruitMember.getRecruitBoardId());
    if (recruitBoard.getWriter().getMemberId() != loginUser.getMemberId()) {
      throw new MoyeoError(ErrorName.ACCESS_DENIED, "/myrecruit/posted");
    }

    RecruitMember old = recruitMemberService.findBy(recruitMember.getMemberId(),
        recruitMember.getRecruitBoardId());
    recruitMember.setMember(old.getMember());

    recruitMemberService.update(recruitMember);
    return "redirect:/myrecruit/appl?recruitBoardId=" + recruitMember.getRecruitBoardId();
  }

  @GetMapping("reqpost")
  public void myrequest(Model model, HttpSession session) throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }
    model.addAttribute("myrequest", recruitBoardService.myrequest(loginUser.getMemberId()));
  }
}
