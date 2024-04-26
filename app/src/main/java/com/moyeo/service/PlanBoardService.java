package com.moyeo.service;

import com.moyeo.vo.Pin;
import com.moyeo.vo.PlanBoard;
import com.moyeo.vo.PlanPhoto;
import java.awt.Panel;
import java.util.List;

public interface PlanBoardService {

  List<PlanBoard> list(int recruitBoardId);

  PlanBoard get(int planBoardId);
  PlanBoard get(int planBoardId, String tripDate, double latitude, double longitude);

  void add(PlanBoard planBoard);

  int update(PlanBoard planBOard);

  int delete(int planBoardId, int recruitBoardId);

  PlanPhoto getPhoto(int planPhotoId);

  List<PlanPhoto> getPhotos(int planBoardId);

  int deletePlanPhoto(int planPhotoId);

  List<Pin> pinList(int recruitBoardId, String tripDate);


}
