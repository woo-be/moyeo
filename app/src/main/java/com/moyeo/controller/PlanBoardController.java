package com.moyeo.controller;

import com.amazonaws.services.s3.internal.eventstreaming.Message;
import com.moyeo.dao.PlanBoardDao;
import com.moyeo.service.MessageService;
import com.moyeo.service.PlanBoardService;
import com.moyeo.vo.Member;
import com.moyeo.vo.Msg;
import com.moyeo.vo.PlanBoard;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/plan")
@RequiredArgsConstructor
@Controller
public class PlanBoardController {
  private final PlanBoardService planBoardService;
  private final static Log log = LogFactory.getLog(PlanBoardController.class);

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


}
