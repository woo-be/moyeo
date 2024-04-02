package com.moyeo.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
@Data
public class ReviewBoard implements Serializable {

  private static final long serialVersionUID = 100L;

  private int reviewBoardId;    // 여행 후기 게시판 식별자
  private int themeId;           // 테마 식별자
  private int memberId;          // 회원 식별자
  private int regionId;          // 지역 식별자
  private String title;           // 제목
  private Date createdDate;       // 게시일
  private String content;         // 내용
  private float latitude;         // 위도
  private float longitude;        // 경도
  private int views;              // 조회수
  private int likeCount;
  private String themeName;
  private String regionName;
  private Member writer;
  private List<ReviewPhoto> photos;
  private List<ReviewComment> commentList;


}

