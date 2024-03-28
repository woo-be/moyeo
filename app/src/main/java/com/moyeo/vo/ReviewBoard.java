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
public class ReviewBoard implements Serializable {

  private static final long serialVersionUID = 100L;

  private int review_board_id;    // 여행 후기 게시판 식별자
  private int theme_id;           // 테마 식별자
  private int member_id;          // 회원 식별자
  private int region_id;          // 지역 식별자
  private String title;           // 제목
  private int created_date;       // 게시일
  private String content;         // 내용
  private float latitude;         // 위도
  private float longitude;        // 경도
  private int views;              // 조회수

}

