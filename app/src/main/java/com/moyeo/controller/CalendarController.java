package com.moyeo.controller;

import com.moyeo.service.RecruitBoardService;
import com.moyeo.vo.Member;
import com.moyeo.vo.RecruitBoard;
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
@RequestMapping("practice")
public class CalendarController {

  private static final Log log = LogFactory.getLog(CalendarController.class);
  private final RecruitBoardService recruitBoardService;

  @GetMapping("calendar")
  public void Calendar(int recruitBoardId, Model model) {

    RecruitBoard team = recruitBoardService.get(recruitBoardId);

    log.debug(team);

    model.addAttribute("team", team);
  }

  @GetMapping("planBoardList")
  public void planBoardList(int recruitBoardId, String date, Model model, HttpSession session) {
    Member loginUser = (Member) session.getAttribute("loginUser");
    log.debug(loginUser);
    model.addAttribute("recruitBoardId", recruitBoardId);
    model.addAttribute("date", date);
    model.addAttribute("nickname", loginUser.getNickname());
  }
}
