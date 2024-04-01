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
public class ReviewComment implements Serializable {

  private static final long serialVersionUID = 100L;

  private int review_comment_id;      // 여행후기게시판 댓글 식별자
  private int member_id;              // 회원 식별자
  private int review_board_id;        // 여행후기게시판 식별자
  private String content;             // 여행후기게시판 댓글 내용
  private Date created_date;          // 여행후기게시판 댓글 작성일

}
