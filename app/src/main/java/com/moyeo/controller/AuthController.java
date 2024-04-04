package com.moyeo.controller;

import com.moyeo.service.MemberService;
import com.moyeo.vo.Member;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {

  private static final Log log = LogFactory.getLog(AuthController.class);
  private final MemberService memberService;

  // templates의 auth파일에서 form.html 에서 폼을 가져온다.
  @GetMapping("form")
  public void form(@CookieValue(required = false) String email, Model model){
    model.addAttribute("email", email);
  }

  // templates의 auth파일에서 login.html 에서 로그인성공 폼을 가져온다.
  @PostMapping("login")
  public String login(
      String email,
      String password,
      String saveEmail,
      HttpServletResponse response,
      HttpSession session) {

    if (saveEmail != null) {
      Cookie cookie = new Cookie("email", email);
      cookie.setMaxAge(60 * 60 * 24 * 7);
      response.addCookie(cookie);
    } else {
      Cookie cookie = new Cookie("email", "");
      cookie.setMaxAge(0);
      response.addCookie(cookie);
    }

    Member member = memberService.get(email, password);
    if (member != null) {
      session.setAttribute("loginUser", member);
      // 로그인 성공 시 home.html로 리디렉션
      return "redirect:/home";
    } else {
      // 로그인 실패 시 로그인 페이지로 다시 이동
      return "redirect:/auth/form";
    }
  }

  @GetMapping("logout")
  public String logout(HttpSession session) throws Exception {
    session.invalidate();
    return "redirect:/home";
  }


}
