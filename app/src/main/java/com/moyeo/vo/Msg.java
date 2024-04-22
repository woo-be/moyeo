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
public class Msg implements Serializable {

  private static final long serialVersionUID = 100L;

  private int msg_id;               // 메세지 식별자
  private int recruit_board_id;     // 여행멤버모집게시판 식별자
  private int member_id;            // 회원 식별자
  private String content;           // 채팅 내용
  private Date time;                // 채팅 보낸시간

}
