package com.moyeo.controller;

import com.moyeo.security.MemberUserDetails;
import com.moyeo.service.MemberService;
import com.moyeo.vo.Member;
import com.moyeo.vo.MoyeoError;
import java.util.HashMap;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
  @PostMapping("loginSuccess")
  @ResponseBody
  public HashMap<String, String> login(
      String email,
      @AuthenticationPrincipal MemberUserDetails principal,
      String saveEmail,
      HttpServletResponse response,
      Model model,
      HttpSession session) throws MoyeoError {
    HashMap<String, String> map = new HashMap<>();

    log.debug("로그인 성공!!!");

    log.debug(saveEmail);
    log.debug(principal);

    if (saveEmail != null) {
      Cookie cookie = new Cookie("email", principal.getUsername());
      cookie.setMaxAge(60 * 60 * 24 * 7);
      response.addCookie(cookie);
    } else {
      Cookie cookie = new Cookie("email", "");
      cookie.setMaxAge(0);
      response.addCookie(cookie);
    }
    map.put("login","success");
    return map;
  }

  @GetMapping("/logout")
  public String logout(HttpSession session, HttpServletResponse response) {
    // 세션을 무효화하여 로그아웃
    session.invalidate();
    // 쿠키를 삭제하기 위해 빈 문자열과 유효 시간을 0으로 설정
    // 아이디 저장 나오게 하고싶으면 이부분 삭제
    Cookie cookie = new Cookie("email", "");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
    // 로그아웃 후 리다이렉트
    return "redirect:/home";
  }

}
