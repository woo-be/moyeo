package com.moyeo.controller;

import com.moyeo.annotation.LoginUser;
import com.moyeo.security.MemberUserDetails;
import com.moyeo.service.ReviewScrapService;
import com.moyeo.vo.Member;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    }else{
      reviewScrapService.delete(loginUser.getMemberId(), reviewBoardId);
    }

    return "redirect:/review/view?reviewBoardId=" + reviewBoardId;
  }

  @PostMapping("deleteAll")
  @ResponseBody
  public Map<String, String> deleteAll(int[] reviewBoardIdList, /*@LoginUser Member loginUser*/ HttpSession session){

    Member loginUser = (Member) session.getAttribute("loginUser");
    log.debug(reviewBoardIdList);

    reviewScrapService.deleteAll(reviewBoardIdList, loginUser.getMemberId());

    Map<String, String> map = new HashMap<>();
    map.put("url", "/myreview/scrap");

    return map;
  }
}
