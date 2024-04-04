package com.moyeo.controller;

import com.moyeo.service.MemberService;
import com.moyeo.vo.Member;
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
@RequestMapping("/auth")
public class AuthController {

  private static final Log log = LogFactory.getLog(AuthController.class);

  // templates의 auth파일에서 form.html 에서 폼을 가져온다.
  @GetMapping("form")
  public void form(String email, Model model){
    model.addAttribute("email", email);
  }

  // templates의 auth파일에서 login.html 에서 로그인성공 폼을 가져온다.
  @PostMapping("login")
  public String login(Member member) {

    String id = member.getEmail();
    String password = member.getPassword();

    return "redirect:../index.html";
  }


}
