package com.moyeo.controller;

import com.moyeo.service.RecruitBoardService;
import com.moyeo.vo.Member;
import com.moyeo.vo.RecruitBoard;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("myrecruit")
public class MypageRecruitController {

  private final static Log log = LogFactory.getLog(MypageRecruitController.class);
  private final RecruitBoardService recruitBoardService;

  @GetMapping("posted")
  public void mypost(Model model, HttpSession session) throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인 하시기 바랍니다.");
    }
    model.addAttribute("mypost", recruitBoardService.mypost(loginUser.getMemberId()));
  }

  @GetMapping("reqpost")
  public void myrequest(Model model, HttpSession session) throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인 하시기 바랍니다.");
    }
    model.addAttribute("myrequest", recruitBoardService.myrequest(loginUser.getMemberId()));
  }
}
