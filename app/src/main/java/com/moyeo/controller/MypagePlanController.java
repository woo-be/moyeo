package com.moyeo.controller;

import com.moyeo.service.RecruitBoardService;
import com.moyeo.vo.ErrorName;
import com.moyeo.vo.Member;
import com.moyeo.vo.MoyeoError;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
@RequestMapping("myplan")
public class MypagePlanController {

  private final RecruitBoardService recruitBoardService;
  private final static Log log = LogFactory.getLog(MypagePlanController.class);

  @GetMapping("list")
  public void listHtml(Model model, HttpSession session) throws Exception {

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }
    
    log.debug("html 반환함");

  }

  @PostMapping("list")
  @ResponseBody
  public Object list(Model model, HttpSession session) throws Exception {

    Member loginUser = (Member) session.getAttribute("loginUser");

    return recruitBoardService.teamlist(loginUser.getMemberId());
  }

}
