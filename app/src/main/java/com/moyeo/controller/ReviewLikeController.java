package com.moyeo.controller;

import com.moyeo.service.ReviewLikeService;
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
@RequestMapping("review/like")
public class ReviewLikeController {

  private final ReviewLikeService reviewLikeService;
  private final Log log = LogFactory.getLog(ReviewLikeController.class);

  @GetMapping("add")
  public String add(HttpSession session, int reviewBoardId) {
    Member loginUser = (Member) session.getAttribute("loginUser");
    int checked = reviewLikeService.get(loginUser.getMemberId(), reviewBoardId);

    if(checked != 1){
      reviewLikeService.add(loginUser.getMemberId(), reviewBoardId);
    }

    return "redirect:/review/view?reviewBoardId=" + reviewBoardId;
  }

}
