package com.moyeo.vo;

import java.io.Serializable;
import lombok.Data;

@Data
public class Member implements Serializable {

  // 직렬화 해준다. 버전관리, 안전성, 명시성을 위해 설정해준다.
  private static final long serialVersionUID = 100L;

  private String email;
  private String name;
  private String password;
  private String createdDate;


}
