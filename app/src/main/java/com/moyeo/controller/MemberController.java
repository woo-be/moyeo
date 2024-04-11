package com.moyeo.controller;

import com.moyeo.service.MemberService;
import com.moyeo.service.StorageService;
import com.moyeo.vo.Member;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController implements InitializingBean{

  private static final Log log = LogFactory.getLog(MemberController.class);
  private final MemberService memberService;
  private final StorageService storageService;
  private String uploadDir;

  @Value("${ncp.ss.bucketname}")
  private String bucketName;

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
  public String add(Member member, MultipartFile file) throws Exception {
    if (file.getSize() > 0) {
      String filename = storageService.upload(this.bucketName, this.uploadDir, file);
      member.setPhoto(filename);
    }

    memberService.add(member);
    // 메인페이지로 이동
    return "redirect:../index.html";
  }

  @PostMapping("update")
  public String update(Member member, MultipartFile file, HttpSession session) throws Exception {

    Integer loginedMemberId = (Integer) session.getAttribute("loginedMemberId");

    Member old = memberService.get(loginedMemberId);
    if (old == null) {
      throw new Exception("회원번호가 유효하지 않습니다");
    }

    if (file != null) {
      String filename = storageService.upload(this.bucketName, this.uploadDir, file);
      member.setPhoto(filename);
      storageService.delete(this.bucketName, this.uploadDir, old.getPhoto());
    } else {
      member.setPhoto(old.getPhoto());
    }
    // 이전 회원 정보의 회원번호를 그대로 사용하여 회원 정보를 업데이트합니다.
    member.setMemberId(loginedMemberId);

    memberService.update(member);
    session.removeAttribute("loginUser");
    session.setAttribute("loginUser", member);
    // 업데이트 후 메인페이지로 이동
    return "redirect:../index.html";
  }

  // 현재 사용 안함
  @GetMapping("list")
  public void list(Model model) {
    model.addAttribute("list", memberService.list());

  }


  //    /member/userInfo.html에 attribute로 member 보내주기
  @GetMapping("view")
  public void view(Integer no, Model model) throws Exception {

    Member member = memberService.get(no);

    model.addAttribute("member", member);
  }


  // session에 로그인한 loginUser의 정보를 member에 포장해 member/userInfo로 보내준다.
  @GetMapping("/userInfo")
  public String userInfo(HttpSession session, Model model) {
    // 세션에서 회원 정보 가져오기
    Member member = (Member) session.getAttribute("loginUser");

    if (member != null) {
      // 세션에 저장된 회원 정보가 있을 경우 모델에 추가하여 userInfo로 전달
      model.addAttribute("member", member);
      // 회원 정보를 출력하는 페이지로 이동
      return "member/userInfo";
    } else {
      // 세션에 저장된 회원 정보가 없을 경우 예외 처리 또는 다른 처리 수행
      // 로그인 페이지로 리다이렉트 또는 다른 페이지로 이동
      return "redirect:/auth/form";
    }
  }

  //  /member/delete.html 에서
  @GetMapping("delete")
  public String delete(@RequestParam("id") Integer no, HttpSession session) throws Exception {
    if (no == null) {
      throw new IllegalArgumentException("회원번호가 유효하지 않습니다!!!!");
    }

    Member member = memberService.get(no);
    if(member == null) {
     throw new Exception("회원번호가 유효하지 않습니다!!!!");
    }

    memberService.delete(no);

    String filename = member.getPhoto();
    if (filename != null) {
      storageService.delete(this.bucketName, this.uploadDir, member.getPhoto());
    }
    session.removeAttribute("loginUser");

    return "redirect:../index.html";
  }

  @GetMapping("mypage")
  public String mypage(HttpSession session, Model model) {
    // 세션에서 회원 정보 가져오기
    Member member = (Member) session.getAttribute("loginUser");

    if (member != null) {
      // 세션에 저장된 회원 정보가 있을 경우 모델에 추가하여 userInfo로 전달
      model.addAttribute("member", member);
      // 회원 정보를 출력하는 페이지로 이동
      return "member/mypage";
    } else {
      // 세션에 저장된 회원 정보가 없을 경우 예외 처리 또는 다른 처리 수행
      // 로그인 페이지로 리다이렉트 또는 다른 페이지로 이동
      return "redirect:/auth/form";
    }
  }
}
