package com.moyeo.vo;

import java.io.Serializable;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
@Data
public class Member implements Serializable {
  /*
  Serializable interface 란!
  *우리가 개발을 하다보면, 생성한 객체를 파일로 저장할 일이 있을 수도 있고,
  * 저장한 객체를 읽을 일이 생길수도 있다. 그리고 다른 서버로 보낼 때도 있고,
  * 다른 서버에서 생성한 객체를 받을 일도 있을 것이다. 이럴때 반드시 구현해야 하는
  * 인터페이스가 바로 Serializable 이다. 이 인터페이스를 구현하면 JVM에서 해당 객체를 저장하거나,
  *  다른 서버로 전송할 수 있도록 해준다.
  */

  // 직렬화 해준다. 버전관리, 안전성, 명시성을 위해 설정해준다.
  private static final long serialVersionUID = 100L;

  private int memberId;      // 회원 식별자!
  private String email;       // 이메일
  private int phoneNumber;   // 전화번호
  private String password;    // 비밀번호
  private String name;        // 이름
  private String nickname;    // 닉네임
  private Date birthDate;    // 생년월일
  private boolean gender;     // 성별
  private String photo;       // 프로필 사진
  private String introduce;   // 자기소개

}
