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
public class RecruitScrap implements Serializable {

  private static final long serialVersionUID = 100L;

  private int recruit_board_id;     // 여행멤버모집게시글 식별자
  private int member_id;            // 회원 식별자
}
