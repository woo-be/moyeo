package com.moyeo.controller;


import com.moyeo.service.MessageService;
import com.moyeo.vo.Member;
import com.moyeo.vo.Msg;
import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("chat")
@RequiredArgsConstructor
@Controller
public class MessageController {

  private final MessageService messageService;
  private final static Log log = LogFactory.getLog(PlanBoardController.class);

  @GetMapping("form")
  public void index(
      @RequestParam(defaultValue = "0")int recruitBoardId,
      HttpSession session,
      Model model) {

    Member loginUser = (Member) session.getAttribute("loginUser");

    if(loginUser == null){
      session.setAttribute("message", "로그인 해주세요");
      session.setAttribute("replaceUrl","/auth/login");
    }

    if(recruitBoardId <= 0){
      session.setAttribute("message", "팀 번호가 올바르지 않습니다.");
      session.setAttribute("replaceUrl","/home");
    }

    model.addAttribute("recruitBoardId", recruitBoardId);
    model.addAttribute("nickname", loginUser.getNickname());
    model.addAttribute("memberId", loginUser.getMemberId());

  }

  @PostMapping("addMsg")
  @ResponseBody
  public void addMsg(
      @RequestParam("msg") String message,
      @RequestParam("recruitBoardId") String recruitBoardId,
      HttpSession session) {

    Member loginUser = (Member) session.getAttribute("loginUser");

    Msg msg = Msg
        .builder()
        .msg(message)
        .recruitBoardId(Integer.parseInt(recruitBoardId))
        .memberId(loginUser.getMemberId())
        .nickname(loginUser.getNickname())
        .build();

    log.debug(msg);

    messageService.add(msg);
  }
  @GetMapping("list")
  @ResponseBody
  public List<Msg> list(@RequestParam("recruitBoardId") String recruitBoardId){
    List<Msg> list = messageService.list(Integer.parseInt(recruitBoardId));
    log.debug(list);
    return list;
  }

}
