package com.moyeo.controller;

import com.moyeo.dao.PlanBoardDao;
import com.moyeo.service.PlanBoardService;
import com.moyeo.vo.PlanBoard;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/plan")
@RequiredArgsConstructor
@Controller
public class PlanBoardController {
  private final PlanBoardService planBoardService;
  private final PlanBoardDao planBoardDao;

  @GetMapping("list")
  public void list(Model model) {
    List<PlanBoard> list;
    list = planBoardService.list();
    model.addAttribute("list", list);
  }

  @GetMapping("view")
 public void planBoardGet(int planBoardId, Model model) {
    model.addAttribute("planBoard", planBoardService.get(planBoardId));
 }

  @GetMapping("chat")
  public String index() {
    return "plan/chat";
  }

}
