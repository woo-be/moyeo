package com.moyeo.controller;

import com.moyeo.service.RecruitBoardService;
import com.moyeo.vo.RecruitBoard;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/recruit")
public class RecruitBoardController {

  private static final Log log = LogFactory.getLog(RecruitBoardController.class);
  private final RecruitBoardService recruitBoardService;

  @GetMapping("list")
  public void list(Model model) {
    model.addAttribute("list", recruitBoardService.list());
  }

  public void add() {
  }

  public void form() {
  }

  @GetMapping("view")
  public void view(int no, Model model) throws Exception {
    RecruitBoard recruitBoard = recruitBoardService.get(no);
    if (recruitBoard == null) {
      throw new Exception("유효하지 않은 번호입니다.");
    }
    model.addAttribute("recruitboard", recruitBoard);
  }
  public void update() {
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
