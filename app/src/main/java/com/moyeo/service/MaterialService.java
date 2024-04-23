package com.moyeo.service;

import com.moyeo.vo.Material;
import com.moyeo.vo.MaterialPhoto;
import java.util.List;

public interface MaterialService {
  List<Material> list(int recruitBoardId);

  Material get(int materialId);

  void add(Material material);

  int update(Material material);

  int delete(int materialId, int recruitBoardId);

  MaterialPhoto getMaterialPhoto(int materialPhotoId);

  List<MaterialPhoto> getMaterialPhotos(int materialId);

  int deleteMaterialPhoto(int materialPhotoId);

}
