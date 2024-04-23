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

  private int materialPhotoId;      // 준비물 사진 식별자
  private int materialId;            // 준비물 식별자
  private String materialPhoto;      // 준비물 사진
}
