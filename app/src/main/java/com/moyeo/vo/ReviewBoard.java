package com.moyeo.vo;

import com.amazonaws.services.s3.model.JSONType;
import java.awt.Point;
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
  private double latitude;         // 위도
  private double longitude;        // 경도
  private int views;              // 조회수
  private int likeCount;          // 좋아요수
  private String themeName;       // 테마 이름
  private String regionName;      // 지역 이름
  private Member writer;          // 글쓴이 정보(객체)
  private List<ReviewPhoto> photos;  // 후기 사진들
  private List<ReviewComment> commentList;  // 후기에 달린 댓글들
  private List<Integer> themeList;
  private String address;
}

