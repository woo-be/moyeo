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

  private int review_photo_id;    // 여행후기 사진 식별자
  private int review_board_id;    // 여행후기 게시판 식별자
  private String firstPhoto;           // 여행후기 사진
  private List<String> photoList;
}
