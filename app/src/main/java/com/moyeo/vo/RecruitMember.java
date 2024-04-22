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
public class RecruitMember implements Serializable {

  private static final long serialVersionUID = 100L;

  private int recruitBoardId;     // 여행멤버모집게시글 식별자
  private int memberId;            // 회원 식별자
  private Boolean state;            // 여행 멤버 상태

  private Member member;
}
