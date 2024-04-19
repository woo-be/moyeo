package com.moyeo.service.impl;

import com.moyeo.dao.MaterialDao;
import com.moyeo.dao.MaterialPhotoDao;
import com.moyeo.service.MaterialService;
import com.moyeo.vo.Material;
import com.moyeo.vo.MaterialPhoto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DefaultMaterialService implements MaterialService {

  private final MaterialDao materialDao;
  private final MaterialPhotoDao materialPhotoDao;

  @Override
  public List<Material> list(int recruitBoardId) {
    return materialDao.findAll(recruitBoardId);
  }

  @Override
  public Material get(int materialId) {
    Material material = materialDao.findBy(materialId);
    return material;
  }

  @Transactional
  @Override
  public void add(Material material) {
    materialDao.add(material);

    if (material.getPhotos() != null && material.getPhotos().size() > 0) {
      for (MaterialPhoto materialPhoto : material.getPhotos()) {
        materialPhoto.setMaterialId(material.getMaterialId());
      }

      materialPhotoDao.addAll(material.getPhotos());
    }
  }

  @Transactional
  @Override
  public int update(Material material) {
    int count = materialDao.update(material);
    return count;
  }

  @Transactional
  @Override
  public int delete(int materialId) {
    return materialDao.delete(materialId);
  }


}
