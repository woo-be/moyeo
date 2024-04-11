package com.moyeo.controller;

import com.moyeo.service.RecruitBoardService;
import com.moyeo.service.RecruitScrapService;
import com.moyeo.vo.Member;
import com.moyeo.vo.RecruitScrap;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruitScrap")
public class RecruitScrapController {

  private static final Log log = LogFactory.getLog(RecruitBoardController.class);
  private final RecruitScrapService recruitScrapService;
  private final RecruitBoardService recruitBoardService;

  @GetMapping("add")
  public String add(int recruitBoardId, HttpSession session) throws Exception {

    // 로그인하지 않으면 예외 발생.
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요한 서비스입니다.");
    }

    // recruitScrap 객체 생성
    RecruitScrap recruitScrap = RecruitScrap.builder().
        recruitBoardId(recruitBoardId).
        memberId(loginUser.getMemberId()).
        build();

    // 해당 객체를 recruit_scrap에 추가.
    recruitScrapService.add(recruitScrap);
    // view 페이지로 리다이렉트
    return "redirect:/recruit/view?recruitBoardId=" + recruitBoardId;
  }

  // 로그인한 사용자가 즐겨찾기한 게시글 리스트
  @GetMapping("list")
  public void scrapList(HttpSession session, Model model) {

    Member loginUser = (Member) session.getAttribute("loginUser");

    log.debug("scraplist:" + recruitBoardService.scrapList(loginUser.getMemberId()));

    model.addAttribute("scrapList", recruitBoardService.scrapList(loginUser.getMemberId()));

  }
}
