package com.moyeo.dao;

import com.moyeo.vo.Material;
import com.moyeo.vo.MaterialPhoto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MaterialDao {

  List<Material> findAll(int recruitBoardId);

  Material findBy(int materialId);

  void add(Material material);

  int update(Material material);

  int delete(int materialId, int recruitBoardId);

}
