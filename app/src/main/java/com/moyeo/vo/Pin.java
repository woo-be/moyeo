package com.moyeo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Pin {
  private int planBoardId;
  private String title;               // 말풍선으로 보여줄 제목
  private double latitude;           // 위도
  private double longitude;          // 경도
}
