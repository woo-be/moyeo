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
public class RecruitComment implements Serializable {

  private static final long serialVersionUID = 100L;

  private int recruitCommentId;       // 여행후기게시글 댓글 식별자
  private RecruitBoard recruitBoard;  // 여행멤버모집게시글 식별자
  private Member member;              // 회원 식별자
  private String content;             // 여행멤버모집게시글 댓글 내용
  private Date createdDate;           // 여행멤버모집게시글 댓글 작성일
}
