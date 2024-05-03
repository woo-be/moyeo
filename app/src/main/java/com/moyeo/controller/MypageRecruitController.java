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
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("myrecruit")
public class MypageRecruitController {

  private final static Log log = LogFactory.getLog(MypageRecruitController.class);
  private final RecruitBoardService recruitBoardService;
  private final RecruitMemberService recruitMemberService;

  @GetMapping("posted")
  public void mypost(
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
    int numOfRecord = recruitBoardService.countAllMyPost(loginUser.getMemberId());
    numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    model.addAttribute("mypost", recruitBoardService.mypost(pageNo, pageSize, loginUser.getMemberId()));
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
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

    /* 현재 모집 게시글의 총 모집 인원과 현재 모집 인원을 찾아 모집인원이 가득 찬 경우 예외 발생 */
    RecruitBoard temp = recruitBoardService.findCurrentAndTotalBy(recruitMember.getRecruitBoardId());
    if (temp.getCurrent() >= temp.getRecruitTotal()) {
      throw new MoyeoError("모집인원이 모두 찼습니다.", "/myrecruit/appl?recruitBoardId=" + recruitMember.getRecruitBoardId());
    }

    RecruitMember old = recruitMemberService.findBy(recruitMember.getMemberId(),
        recruitMember.getRecruitBoardId());
    recruitMember.setMember(old.getMember());

    recruitMemberService.update(recruitMember);
    return "redirect:/myrecruit/appl?recruitBoardId=" + recruitMember.getRecruitBoardId();
  }

  @GetMapping("reqpost")
  public void myrequest(
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
    int numOfRecord = recruitBoardService.countAllMyReq(loginUser.getMemberId());
    numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    model.addAttribute("myrequest", recruitBoardService.myrequest(pageNo, pageSize, loginUser.getMemberId()));
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
  }
}
