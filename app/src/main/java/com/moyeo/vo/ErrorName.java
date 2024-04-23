package com.moyeo.vo;

import lombok.Getter;

@Getter
public class ErrorName {

  public static final String LOGIN_REQUIRED = "로그인 후 이용해주세요.";

  public static final String ACCESS_DENIED = "접근 권한이 없습니다.";

  public static final String INVALID_NUMBER = "유효하지 않은 번호입니다.";

  public static final String SELECT_REQUIRED = "삭제할 즐겨찾기 항목을 선택해 주세요.";

  public static final String REJECTED_RECRUIT_MYPOST = "자신의 게시글을 신청할 수 없습니다.";

  public static final String REJECTED_SCRAP_MYPOST = "자신의 게시글을 신청할 수 없습니다.";
}
