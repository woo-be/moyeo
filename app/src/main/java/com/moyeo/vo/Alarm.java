package com.moyeo.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Alarm implements Serializable {

  private static final long serialVersionUID = 100L;

  private int alarmId;     // 알람 식별자
  private int memberId;    // 회원 식별자
  private String content;   // 알림 내용
  private boolean checked;  // 알림 확인 여부

}
