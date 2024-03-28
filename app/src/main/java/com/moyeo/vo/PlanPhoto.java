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
public class PlanPhoto implements Serializable {

  private static final long serialVersionUID = 100L;

  private int plan_photo_id;      // 여행계획게시물사진 식별자
  private int plan_board_id;      // 여해예획게시물 식별자
  private String photo;           // 사진
}
