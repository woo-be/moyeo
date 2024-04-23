package com.moyeo.service;

import com.moyeo.vo.PlanBoard;

import com.moyeo.vo.PlanPhoto;
import java.awt.Panel;

import java.util.List;

public interface PlanBoardService {

  List<PlanBoard> list();

  PlanBoard get(int planBoardId);


  void add(PlanBoard planBoard);

  int update(PlanBoard planBOard);

  int delete(int planBoardId, int recruitBoardId);

  PlanPhoto getPhoto(int planPhotoId);

  List<PlanPhoto> getPhotos(int planBoardId);

  int deletePlanPhoto(int planPhotoId);

}
