package com.moyeo.controller;

import com.moyeo.service.MemberService;
import com.moyeo.service.StorageService;
import com.moyeo.vo.Member;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

  private static final Log log = LogFactory.getLog(AuthController.class);
  private final MemberService memberService;

  // 회원가입 폼 불러오기
  @GetMapping("/signup")
  public void form() throws Exception {
  }

  // 회원가입 폼 제출
  @PostMapping("/add")
  public String signup(Member member){

    memberService.add(member);
    // 메인페이지로 이동
    return "redirect:../index.html";
  }


}
