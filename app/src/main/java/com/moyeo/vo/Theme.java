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
public class Theme implements Serializable {

  private static final long serialVersionUID = 100L;

  private int theme_id;         // 테마 식별자
  private String theme_name;    // 테마 이름
}
