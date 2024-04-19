package com.moyeo.controller;

import com.moyeo.dao.PlanBoardDao;
import com.moyeo.service.PlanBoardService;
import com.moyeo.vo.Member;
import com.moyeo.vo.PlanBoard;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/plan")
@RequiredArgsConstructor
@Controller
public class PlanBoardController {
  private static final Log log = LogFactory.getLog(PlanBoardController.class);
  private final PlanBoardService planBoardService;

  @GetMapping("list")
  public void list(
      int recruitBoardId,
      Model model) {
    List<PlanBoard> list;
    list = planBoardService.list(recruitBoardId);

    log.debug("planBoard = " + list);
    model.addAttribute("list", list);
    model.addAttribute("recruitBoardId", recruitBoardId);
  }

  @GetMapping("view")
 public void view(int planBoardId, Model model) {
    model.addAttribute("planBoard", planBoardService.get(planBoardId));
 }

 @GetMapping("form")
 public void form(
     int recruitBoardId,
     Model model) throws Exception {

    model.addAttribute("recruitBoardId", recruitBoardId);

 }

 @PostMapping("add")
 public String add(
     PlanBoard planBoard,
     HttpSession session,
     Model model) throws Exception {

   Member loginUser = (Member) session.getAttribute("loginUser");
   if (loginUser == null) {
     session.setAttribute("message", "로그인 해주세요");
     session.setAttribute("replaceUrl", "/auth/form");
   }

   model.addAttribute("recruitBoardId", planBoard.getRecruitBoardId());


   planBoardService.add(planBoard);

   return "redirect:view?planBoardId=" + planBoard.getPlanBoardId();
 }

 @PostMapping("update")
 public String update(
     PlanBoard planBoard,
     HttpSession session,
     Model model) throws Exception {

    model.addAttribute("recruitBoardId", planBoard.getRecruitBoardId());

   Member loginUser = (Member) session.getAttribute("loginUser");
   if (loginUser == null) {
     session.setAttribute("message", "로그인 해주세요");
     session.setAttribute("replaceUrl", "/auth/form");
   }

   PlanBoard old = planBoardService.get(planBoard.getPlanBoardId());
   if (old == null) {
     throw new Exception("번호가 유효하지 않습니다");
   }

   planBoardService.update(planBoard);

   return "redirect:view?planBoardId=" + planBoard.getPlanBoardId();
 }

 @PostMapping("updateForm")
 public void updateForm(int recruitBoardId, int planBoardId, HttpSession session, Model model) {

    PlanBoard planBoard = planBoardService.get(planBoardId);

    model.addAttribute("recruitBoardId", recruitBoardId);
    model.addAttribute("updatePlanBoard", planBoard);

 }

  @GetMapping("chat")
  public String index() {
    return "plan/chat";
  }

}
