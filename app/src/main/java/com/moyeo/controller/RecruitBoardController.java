package com.moyeo.controller;

import com.moyeo.service.RecruitBoardService;
import com.moyeo.service.RegionService;
import com.moyeo.service.ThemeService;
import com.moyeo.vo.Member;
import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.Theme;
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
@RequestMapping("/recruit")
public class RecruitBoardController {

  private static final Log log = LogFactory.getLog(RecruitBoardController.class);
  private final RecruitBoardService recruitBoardService;
  private final RegionService regionService;
  private final ThemeService themeService;

  @GetMapping("list")
  public void list(
      Model model,
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "10") int pageSize) {

    if (pageSize < 10 || pageSize > 20) {
      pageSize = 10;
    }
    if (pageNo < 1){
      pageNo = 1;
    }

    int numOfRecord = recruitBoardService.countAll();
    int numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    model.addAttribute("list", recruitBoardService.list(pageNo, pageSize));

    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
  }

  @PostMapping("add")
  public String add(
      RecruitBoard board,
      int regionId,
      int themeId) {
    board.setRegion(regionService.get(regionId));
    board.setTheme(themeService.get(themeId));

    // 임시 멤버 객체, 세션에서 받아오도록 해야 함.
    Member loginUser = Member.builder()
        .memberId(6)
        .name("비트").build();

    board.setWriter(loginUser);

    recruitBoardService.add(board);

    return "redirect:list";
  }

  @GetMapping("addForm")
  public String addForm() throws Exception {
    return "recruit/addForm";
  }

  @PostMapping("updateForm")
  public String updateForm(int recruitBoardId, Model model) throws Exception {
    RecruitBoard board = recruitBoardService.get(recruitBoardId);
    log.debug(board);
    model.addAttribute("board", board);
    return "recruit/updateForm";
  }

  public void update(RecruitBoard board) {
    recruitBoardService.update(board);
  }

  @GetMapping("view")
  public void view(int no, Model model) throws Exception {
    RecruitBoard recruitBoard = recruitBoardService.get(no);
    if (recruitBoard == null) {
      throw new Exception("유효하지 않은 번호입니다.");
    }
    model.addAttribute("recruitboard", recruitBoard);
  }

  @GetMapping("delete")
  public String delete(int no) throws Exception {
    RecruitBoard recruitBoard = recruitBoardService.get(no);
    if (recruitBoard == null) {
      throw new Exception("유효하지 않은 번호입니다.");
    }

    recruitBoardService.delete(no);
    return "redirect:list";
  }
}
