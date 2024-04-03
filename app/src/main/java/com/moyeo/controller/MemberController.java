package com.moyeo.controller;

import com.moyeo.service.MemberService;
import com.moyeo.service.StorageService;
import com.moyeo.vo.Member;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
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
public class MemberController implements InitializingBean{

  private static final Log log = LogFactory.getLog(MemberController.class);
  private final MemberService memberService;


  // 빈 초기화
  @Override
  public void afterPropertiesSet() throws Exception {
  }


  // 회원가입 폼 불러오기
  @GetMapping("signup")
  public void form() throws Exception {
  }


  // 회원가입 폼 제출
  @PostMapping("add")
  public String add(Member member) throws Exception {

    memberService.add(member);
    // 메인페이지로 이동
    return "redirect:../index.html";
  }

  @PostMapping("update")
  public String update(Member member) throws Exception {

    memberService.update(member);
    // 업데이트 후 메인페이지로 이동
    return "redirect:index.html";
  }

  @GetMapping("list")
  public void list(Model model) {
    model.addAttribute("list", memberService.list());

  }


  //    /member/view.html 에서 attribute로 member vo 보내주기
  @GetMapping("view")
  public void view(Integer no, Model model) throws Exception {

    Member member = memberService.get(no);

    model.addAttribute("member", member);
  }


  //  /member/delete.html 에서
  @GetMapping("delete")
  public String delete(int no) throws Exception {
    Member member = memberService.get(no);
    if(member == null) {
     throw new Exception("회원번호가 유효하지 않습니다!!!!");
    }

    memberService.delete(no);

    return "redirect:../index.html";
  }


}
