package com.moyeo.controller;

import com.moyeo.service.MemberService;
import com.moyeo.service.StorageService;
import com.moyeo.vo.Member;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController implements InitializingBean{

  private static final Log log = LogFactory.getLog(MemberController.class);
  private final MemberService memberService;
  private final StorageService storageService;
  private final PasswordEncoder passwordEncoder;
  private String uploadDir;

  @Value("${ncp.ss.bucketname}")
  private String bucketName;

  // 빈 초기화
  @Override
  public void afterPropertiesSet() throws Exception {
    this.uploadDir = "member/";

    log.debug(String.format("uploadDir: %s", this.uploadDir));
    log.debug(String.format("bucketname: %s", this.bucketName));
  }


  // 회원가입 폼 불러오기
  @GetMapping("signup")
  public String signup(HttpSession session, Model model) throws Exception {
    Member newMember = (Member) session.getAttribute("newMember");

    if (newMember == null) {
      // newMember가 존재하지 않으면 빈 문자열로 초기화된 Member 객체를 사용합니다.
      newMember = new Member();
      newMember.setName("");
      newMember.setEmail("");
    }

    model.addAttribute("newMember", newMember); // newMember를 모델에 추가합니다.
    return "member/signup"; // 회원가입 페이지로 이동합니다.
  }


  // 회원가입 폼 제출
  @PostMapping("add")
  public String add(Member member, MultipartFile file) throws Exception {
    log.debug("asdasdasdasdasdasd");

    if (file.getSize() > 0) {
      String filename = storageService.upload(this.bucketName, this.uploadDir, file);
      member.setPhoto(filename);
    }

    // spring-security에서 비밀번호를 암호화해서 넣어준다.
    member.setPassword(passwordEncoder.encode(member.getPassword()));
    
    memberService.add(member);
    log.debug("asdasdasdasdasdasd");
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

    log.debug(file);

    if (file.getSize() > 0) {
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
    return "redirect:mypage";
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
  @PostMapping("delete")
  @ResponseBody
  public String delete(@RequestParam("id") Integer no, HttpSession session) throws Exception {
    if (no == null) {
      throw new IllegalArgumentException("회원번호가 유효하지 않습니다!!!!");
    }

    Member member = memberService.get(no);

    if(member == null) {
     throw new Exception("회원번호가 유효하지 않습니다!!!!");
    }

    String filename = member.getPhoto();
    if (filename != null) {
      storageService.delete(this.bucketName, this.uploadDir, member.getPhoto());
    }

    LocalDate currentDate = LocalDate.now();
    System.out.println("현재 날짜: " + currentDate);

    member.setPassword(passwordEncoder.encode("moyeo" + member.getMemberId()));
    member.setEmail("user" + member.getMemberId());
    member.setName("user" + member.getMemberId());
    member.setBirthdate(Date.valueOf(currentDate));
    member.setPhoto("NULL");
    member.setPhoneNumber("delete" + member.getMemberId());
    member.setIntroduce("탈퇴 한 계정");

    memberService.delete(member);
    session.removeAttribute("loginUser");

    return "/index.html";
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

  // 회원가입 폼 불러오기
  @GetMapping("findEmail")
  public void findEmail() throws Exception {
  }

  @PostMapping("findByEmail")
  public void findByEmail(Member member, Model model) {
    Member findMember = memberService.get(member.getPhoneNumber(), member.getName(), member.getBirthdate());

    log.debug(String.format("%s     %s        %S",member.getPhoneNumber(), member.getName(), member.getBirthdate()));

    model.addAttribute("findMember", findMember);

  }

  // 비밀번호 update하기위해 개인정보입력 form 출력
  @GetMapping("findPassword")
  public void findPassword() throws Exception{
  }

  // findPassword.html에서 post방식으로 정보를 받아서 => updatePassword.html으로 정보를 보내준다
  @PostMapping("findPassword")
  public String searchPassword(@RequestParam("email") String email,
                              @RequestParam("phoneNumber") String phoneNumber,
                              @RequestParam("name") String name,
                              @RequestParam("birthdate") Date birthdate,
                              Model model, HttpSession session) {
    Member updatedMember = memberService.findBy(email, name, phoneNumber, birthdate);
    if(updatedMember == null){
      // 에러
    }
    session.setAttribute("updatedMember", updatedMember);

    return "redirect:/member/updatePassword";
  }

  // updatePassword.html form을 가져온다.
  @GetMapping("updatePassword")
  public void updatePassword() {
  }

  // updatePassword.html에 적힌 정보를 newPassword에 정보를 가지고와 member에 newPassword를 넣어준다.
  @PostMapping("updatePassword")
  public String updatePassword(Member member, Model model, @RequestParam("newPassword")String newPassword) {
    member.setPassword(passwordEncoder.encode(newPassword));
    memberService.updatePassword(member);

//    if(updatedMember == null){
//      // 에러
//    }
    return "redirect:/home";
  }

}
