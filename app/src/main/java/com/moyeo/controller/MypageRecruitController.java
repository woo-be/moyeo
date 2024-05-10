package com.moyeo.controller;

import com.moyeo.service.AlarmService;
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
  private final AlarmService alarmService;

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

    if (numOfRecord != 0) { // 해당하는 데이터가 하나라도 있다면,
      numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);
    }

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    /* 페이징 페이지 숫자 버튼 */
    int[] pageButtons; // 페이징 페이지 숫자 버튼

    if (numOfPage >= 5) { // a. 게시판 페이지가 5개 이상일 때,
      pageButtons = new int[5]; // 페이지 숫자 버튼의 개수를 5개로 함.

      if (pageNo <= 3) { // 1. 현재 페이지가 시작페이지에서 3페이지 이내의 페이지일 때,
        for (int i = 0; i < 5; i++) { // 숫자 버튼이 1부터 시작하도록 함.
          pageButtons[i] = i + 1;
        }
      } else if (pageNo >= (numOfPage - 2)) { // 2. 현재 페이지가 끝페이지에서 3페이지 이내의 페이지일 때,
        int temp = numOfPage;
        for (int i = 4; i >= 0; i--) { // 숫자 버튼이 끝페이지 -4 부터 시작하도록 함.
          pageButtons[i] = temp--;
        }
      } else { // 3. 그 외의 경우,
        int temp = pageNo - 2;
        for (int i = 0; i < 5; i++) { // 숫자 버튼의 가운데 버튼이 현재페이지를 가리키도록 함.
          pageButtons[i] = temp++;
        }
      }
    } else { // b. 게시판 페이지가 5개 미만일 때,
      pageButtons = new int[numOfPage]; // 페이지 숫자 버튼의 개수를 전체 페이지 개수로 함.
      for (int i = 0; i < numOfPage; i++) { // 숫자 버튼이 1부터 시작하도록 함.
        pageButtons[i] = i + 1;
      }
    }

    model.addAttribute("mypost", recruitBoardService.mypost(pageNo, pageSize, loginUser.getMemberId()));
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
    model.addAttribute("pageButtons", pageButtons); // 페이지 숫자 버튼
  }

  @GetMapping("/appl")
  public void appllist(Model model, @RequestParam(required = false, defaultValue = "0") int alarmId, int recruitBoardId, HttpSession session) throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }

    RecruitBoard recruitBoard = recruitBoardService.get(recruitBoardId);
    if (recruitBoard.getWriter().getMemberId() != loginUser.getMemberId()) {
      throw new MoyeoError(ErrorName.ACCESS_DENIED, "/myrecruit/posted");
    }

    if (alarmId != 0) {
      if (!alarmService.getStatus(alarmId)) {
        alarmService.update(alarmId);
      }
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

    /* 현재 모집 게시글의 총 모집 인원과 현재 모집 인원을 찾아 모집인원이 가득 찼을 때 수락을 누르면 예외 발생 */
    RecruitBoard temp = recruitBoardService.findCurrentAndTotalBy(recruitMember.getRecruitBoardId());
    if (temp.getCurrent() >= temp.getRecruitTotal() && recruitMember.getState()) {
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

    if (numOfRecord != 0) { // 해당하는 데이터가 하나라도 있다면,
      numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);
    }

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    /* 페이징 페이지 숫자 버튼 */
    int[] pageButtons; // 페이징 페이지 숫자 버튼

    if (numOfPage >= 5) { // a. 게시판 페이지가 5개 이상일 때,
      pageButtons = new int[5]; // 페이지 숫자 버튼의 개수를 5개로 함.

      if (pageNo <= 3) { // 1. 현재 페이지가 시작페이지에서 3페이지 이내의 페이지일 때,
        for (int i = 0; i < 5; i++) { // 숫자 버튼이 1부터 시작하도록 함.
          pageButtons[i] = i + 1;
        }
      } else if (pageNo >= (numOfPage - 2)) { // 2. 현재 페이지가 끝페이지에서 3페이지 이내의 페이지일 때,
        int temp = numOfPage;
        for (int i = 4; i >= 0; i--) { // 숫자 버튼이 끝페이지 -4 부터 시작하도록 함.
          pageButtons[i] = temp--;
        }
      } else { // 3. 그 외의 경우,
        int temp = pageNo - 2;
        for (int i = 0; i < 5; i++) { // 숫자 버튼의 가운데 버튼이 현재페이지를 가리키도록 함.
          pageButtons[i] = temp++;
        }
      }
    } else { // b. 게시판 페이지가 5개 미만일 때,
      pageButtons = new int[numOfPage]; // 페이지 숫자 버튼의 개수를 전체 페이지 개수로 함.
      for (int i = 0; i < numOfPage; i++) { // 숫자 버튼이 1부터 시작하도록 함.
        pageButtons[i] = i + 1;
      }
    }

    model.addAttribute("myrequest", recruitBoardService.myrequest(pageNo, pageSize, loginUser.getMemberId()));
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
    model.addAttribute("pageButtons", pageButtons); // 페이지 숫자 버튼
  }
}
