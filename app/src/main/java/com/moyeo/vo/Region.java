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
public class Region implements Serializable {

  private static final long serialVersionUID = 100L;

  private int region_id;        // 지역 식별자
  private String region_name;   // 지역 이름
}
