package com.moyeo.dao;

import com.moyeo.vo.PlanPhoto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlanPhotoDao {
  void add(PlanPhoto planPhoto);

  int addAll(List<PlanPhoto> planPhotos);

  List<PlanPhoto> findAllByPlanBoardId(int planBoardId);

  PlanPhoto findByPlanPhotoId(int planPhotoId);

  int delete(int planPhotoId);

  int deleteAll(int planBoardId);

}
