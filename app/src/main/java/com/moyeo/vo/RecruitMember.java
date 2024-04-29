package com.moyeo.vo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RecruitMember implements Serializable {

  private static final long serialVersionUID = 100L;

  private int recruitBoardId;     // 여행멤버모집게시글 식별자
  private int memberId;            // 회원 식별자
  private Boolean state;            // 여행 멤버 상태

  private LocalDateTime createdDate;             // 신청일자

  private Member member;
}
