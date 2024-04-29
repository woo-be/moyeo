package com.moyeo.controller;

import com.moyeo.service.ReviewScrapService;
import com.moyeo.vo.Member;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("scrap")
public class ReviewScrapController {

  private final Log log = LogFactory.getLog(ReviewScrapController.class);
  private final ReviewScrapService reviewScrapService;

  @GetMapping("add")
  public String add(
      HttpSession session,
      int reviewBoardId
    ) {
    Member loginUser = (Member) session.getAttribute("loginUser");

    int checked = reviewScrapService.get(loginUser.getMemberId(), reviewBoardId);

    if(checked != 1) {
      reviewScrapService.add(loginUser.getMemberId(), reviewBoardId);
    }

    return "redirect:/review/view?reviewBoardId=" + reviewBoardId;
  }

}
