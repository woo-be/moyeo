package com.moyeo.controller;

import com.moyeo.service.PlanBoardService;
import com.moyeo.vo.PlanBoard;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/plan")
@RequiredArgsConstructor
@Controller
public class PlanBoardController {
  private static final Log log = LogFactory.getLog(PlanBoardController.class);
  private final PlanBoardService planBoardService;

  @GetMapping("list")
  public void list(Model model) {
    List<PlanBoard> list;
    list = planBoardService.list();
    model.addAttribute("list", list);
  }

  @GetMapping("chat")
  public String chat() {
    return "/plan/chat";
//    log.debug(String.format("%s", ));
  }

}
