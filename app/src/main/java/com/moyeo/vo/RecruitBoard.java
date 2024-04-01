package com.moyeo.vo;

import java.io.Serializable;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RecruitBoard implements Serializable {

  private static final long serialVersionUID = 100L;

  private int recruit_board_id;         // 여행멤버모집게시글 식별자
  private Member member;                // 회원 식별자
  private Region region;                // 지역 식별자
  private Theme theme;                  // 테마 식별자

  private String title;                 // 여행멤버모집 게시글 제목
  private String content;               // 여행멤버모집 게시글 내용

  private Date start_date;              // 여행 시작일
  private Date end_date;                // 여행 종료일

  private int recruit_total;            // 모집 인원
  private Date deadline;                // 모집 마감일자
  private boolean state;                // 모집 상태

  private Date created_date;            // 여행멤버모집게시물 작성일자

  private float latitude;               // 위도
  private float longitude;              // 경도

  private int views;                    // 여행멤버모집게시글 조회수
}
