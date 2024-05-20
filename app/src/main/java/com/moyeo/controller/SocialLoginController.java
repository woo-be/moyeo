package com.moyeo.controller;

import com.moyeo.service.MemberService;
import com.moyeo.vo.Member;
import com.moyeo.vo.google.GoogleInfResponse;
import com.moyeo.vo.google.GoogleRequest;
import com.moyeo.vo.google.GoogleResponse;
import java.net.URLEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class SocialLoginController {

  private static final Log log = LogFactory.getLog(SocialLoginController.class);

  @Value("${google.client.id}")
  private String googleClientId;
  @Value("${google.client.pw}")
  private String googleClientPw;

  @Autowired
  private MemberService memberService;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


//  로컬용 구글 소셜 로그인
//  @PostMapping("/auth/login/google")
//  public void loginUrlGoogle(HttpServletResponse response) throws IOException {
//    String reqUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientId
//        + "&redirect_uri=http://localhost:8888/api/v1/oauth2/google&response_type=code&scope=email%20profile%20openid&access_type=offline";
//    response.sendRedirect(reqUrl);
//  }


  @PostMapping("/auth/login/google")
  public void loginUrlGoogle(HttpServletResponse response) throws IOException {
    String reqUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientId
        + "&redirect_uri=http://moyeo.p-e.kr/api/v1/oauth2/google&response_type=code&scope=email%20profile%20openid&access_type=offline";
    response.sendRedirect(reqUrl);
  }



  @GetMapping("/api/v1/oauth2/google")
  public void googleLogin(@RequestParam("code") String authCode,
      HttpSession session,
      HttpServletResponse response) throws IOException {
    String jwtToken = getGoogleJwtToken(authCode);
    GoogleInfResponse userInfo = getUserInfoFromToken(jwtToken);
    Member member = createMemberFromUserInfo(userInfo);
    Member existingMember = memberService.getByEmail(member.getEmail());

    if (existingMember == null) {
      session.setAttribute("newMember", member);
      // 모달 창을 닫고 회원가입 페이지로 이동
      String script = "<script>"
          + "window.close();"
          + "window.opener.location.href='/member/signup';"
          + "</script>";
      response.setContentType("text/html");
      response.getWriter().println(script);
    } else {
      // 모달 창을 닫고 AJAX 요청 보내기
      String email = existingMember.getEmail();
      String script = "<script>"
          + "var xhr = new XMLHttpRequest();"
          + "xhr.open('POST', '/auth/login', true);"
          + "var formData = new FormData();"
          + "formData.append('email', '" + email + "');"
          + "formData.append('password', '" + bCryptPasswordEncoder.encode(existingMember.getPassword()) + "');"
          + "xhr.onreadystatechange = function() {"
          + "  if (xhr.readyState === XMLHttpRequest.DONE) {"
          + "    if (xhr.status === 200) {"
          + "      window.opener.location.href = '/home';" // 홈 페이지로 이동"
          + "    } else {"
          + "      console.error('로그인 요청 실패:', xhr.status, xhr.statusText);"
          + "    }"
          + "    window.close();"
          + "  }"
          + "};"
          + "xhr.send(formData);"
          + "</script>";
      response.setContentType("text/html");
      response.getWriter().println(script);
    }
  }


  // 구글 OAuth를 통해 토큰 가져오기
  private String getGoogleJwtToken(String authCode) {
    RestTemplate restTemplate = new RestTemplate();
    GoogleRequest googleOAuthRequestParam = GoogleRequest.builder()
        .clientId(googleClientId)
        .clientSecret(googleClientPw)
        .code(authCode)
//        로컬용 소셜로그인
//        .redirectUri("http://localhost:8888/api/v1/oauth2/google")
        .redirectUri("http://moyeo.p-e.kr/api/v1/oauth2/google")
        .grantType("authorization_code").build();
    ResponseEntity<GoogleResponse> resultEntity = restTemplate.postForEntity(
        "https://oauth2.googleapis.com/token",
        googleOAuthRequestParam, GoogleResponse.class);
    return resultEntity.getBody().getId_token();
  }

  // 토큰을 통해 사용자 정보 가져오기
  private GoogleInfResponse getUserInfoFromToken(String jwtToken) {
    RestTemplate restTemplate = new RestTemplate();
    Map<String, String> map = new HashMap<>();
    map.put("id_token", jwtToken);
    ResponseEntity<GoogleInfResponse> resultEntity = restTemplate.postForEntity(
        "https://oauth2.googleapis.com/tokeninfo",
        map, GoogleInfResponse.class);
    return resultEntity.getBody();
  }

  // 사용자 정보를 바탕으로 회원 객체 생성
  private Member createMemberFromUserInfo(GoogleInfResponse userInfo) {
    Member member = new Member();
    member.setEmail(userInfo.getEmail());
    member.setName(userInfo.getName());
    // 필요한 경우 추가 정보 설정
    return member;
  }
}
