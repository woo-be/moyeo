package com.moyeo.vo;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PlanBoard implements Serializable {

  private static final long serialVersionUID = 100L;

  private int planBoardId;        // 여행계획게시물 식별자
  private int recruitBoardId;     // 여행멤버모집게시글 식별자
  private String title;             // 여행계획게시물 제목
  private String content;           // 여행계획게시물 내용
  private Date tripDate;           // 여행일자
  private int tripOrder;           // 여행루트순서
  private double latitude;           // 위도
  private double longitude;          // 경도
  private List<PlanPhoto> photos;
}
