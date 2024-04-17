package com.moyeo.controller;

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
@RequestMapping("/myrecruit/scrap")
public class RecruitScrapController {

  private static final Log log = LogFactory.getLog(RecruitBoardController.class);
  private final RecruitScrapService recruitScrapService;

  // 즐겨찾기 추가
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
    // list 페이지로 리다이렉트(임시)
    return "redirect:";
  }

  // 로그인한 사용자가 즐겨찾기한 게시글 리스트
  @GetMapping("")
  public void scrapList(HttpSession session, Model model) {

    Member loginUser = (Member) session.getAttribute("loginUser");

    // 즐겨찾기 리스트를 모델객체의 scarpList에 담음.
    model.addAttribute("scrapList", recruitScrapService.list(loginUser.getMemberId()));
  }

  // 즐겨찾기 삭제 - 선택된 즐겨찾기 항목 삭제
  @GetMapping("delete")
  public String delete(String scrapRecruitBoardIds, // 삭제할 즐겨찾기 모집게시글 번호들을 담는 문자열
      HttpSession session) throws Exception {

    // 아무것도 선택하지 않았다면 예외 발생
    if(scrapRecruitBoardIds == null || scrapRecruitBoardIds.isEmpty()) {
      throw new Exception("삭제할 즐겨찾기 항목을 선택해 주세요.");
    }

    // 각 번호들을 ','를 기준으로 분리하여 배열로 저장한다.
    String[] scrapRecruitBoardIdArr = scrapRecruitBoardIds.split(",");

    Member loginUser = (Member) session.getAttribute("loginUser");

    // 각 번호마다 recruitScrap 객체를 생성하여 해당 객체를 recruit_scrap에서 삭제한다.
    for (String scrapRecruitBoardId : scrapRecruitBoardIdArr) {
      RecruitScrap recruitScrap = RecruitScrap.builder().
          recruitBoardId(Integer.parseInt(scrapRecruitBoardId)).
          memberId(loginUser.getMemberId()).
          build();

      // 해당 recruitBoardId를 가진 즐겨찾기를 제거.
      recruitScrapService.delete(recruitScrap);
    }

    // list로 리다이렉트(임시), 추후에 마이페이지에서 사용.
    return "redirect:";
  }
}
