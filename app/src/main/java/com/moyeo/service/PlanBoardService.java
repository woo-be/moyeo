package com.moyeo.service;

import com.moyeo.vo.PlanBoard;
import com.moyeo.vo.PlanPhoto;
import java.awt.Panel;
import java.sql.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PlanBoardService {

  List<PlanBoard> list(int recruitBoardId);

  PlanBoard get(int planBoardId);

  void add(PlanBoard planBoard);

  int update(PlanBoard planBOard);

  int delete(int planBoardId, int recruitBoardId);

  PlanPhoto getPhoto(int planPhotoId);

  List<PlanPhoto> getPhotos(int planBoardId);

  int deletePlanPhoto(int planPhotoId);

  List<PlanBoard> findByTripDate(@Param("tripDate")String tripDate, @Param("recruitBoardId")int recruitBoardId);
}
