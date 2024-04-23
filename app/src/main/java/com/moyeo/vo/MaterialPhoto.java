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
public class MaterialPhoto implements Serializable {

  private static final long serialVersionUID = 100L;

  private int material_photo_id;      // 준비물 사진 식별자
  private int material_id;            // 준비물 식별자
  private String material_photo;      // 준비물 사진
}
