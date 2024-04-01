package com.moyeo.vo;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
@Data
public class ReviewPhoto implements Serializable {

  private static final long serialVersionUID = 100L;

  private String reviewPhotoId;    // 여행후기 사진 식별자
  private int reviewBoardId;    // 여행후기 게시판 식별자
  private String photo;           // 여행후기 사진
}
