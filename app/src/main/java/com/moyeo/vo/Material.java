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
public class Material implements Serializable {

  private static final long serialVersionUID = 100L;

  private int material_id;          // 준비물 식별자
  private int recruit_board_id;     // 여행멤버모집게시글 식별자
  private String name;              // 준비물 이름
  private String content;           // 준비물 내용
  private int count;                // 준비물 수량
  private boolean state;            // 준비물 상태

}
