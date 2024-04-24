package com.moyeo.controller;

import com.moyeo.service.RecruitBoardService;
import com.moyeo.service.RecruitScrapService;
import com.moyeo.vo.ErrorName;
import com.moyeo.vo.Member;
import com.moyeo.vo.MoyeoError;
import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitScrap;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myrecruit")
public class RecruitScrapController {

  private static final Log log = LogFactory.getLog(RecruitBoardController.class);
  private final RecruitScrapService recruitScrapService;
  private final RecruitBoardService recruitBoardService;

  // 즐겨찾기 추가
  @GetMapping("scrap/add")
  public String add(int recruitBoardId, HttpSession session) throws Exception {

    // 로그인한 상태인지 아닌지 검사.
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }

    // 조회쪽에서 즐겨찾기하는 상황 상정하고 작성
    RecruitBoard recruitBoard = recruitBoardService.get(recruitBoardId);
    if (loginUser.getMemberId() == recruitBoard.getWriter().getMemberId()) {
      throw new MoyeoError(ErrorName.REJECTED_SCRAP_MYPOST, "/recruit/view?recruitBoardId=" + recruitBoardId);
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
  @GetMapping("scrap")
  public void scrapList(
      Model model,
      HttpSession session,
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "10") int pageSize) throws Exception {

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }

    if (pageSize < 10 || pageSize > 20) {
      pageSize = 10;
    }
    if (pageNo < 1){
      pageNo = 1;
    }

    int numOfPage = 1;
    int numOfRecord = recruitScrapService.countAll(loginUser.getMemberId());
    numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    // 즐겨찾기 리스트를 모델객체의 scarpList에 담음.
    model.addAttribute("scrapList", recruitScrapService.list(pageNo, pageSize, loginUser.getMemberId()));
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
  }

  // 즐겨찾기 삭제 - 선택된 즐겨찾기 항목 삭제
  @GetMapping("scrap/delete")
  public String delete(String scrapRecruitBoardIds, // 삭제할 즐겨찾기 모집게시글 번호들을 담는 문자열
      HttpSession session) throws Exception {

    // 아무것도 선택하지 않았다면 예외 발생
    if(scrapRecruitBoardIds == null || scrapRecruitBoardIds.isEmpty()) {
      throw new MoyeoError(ErrorName.SELECT_REQUIRED, "/myrecruit/scrap");
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
