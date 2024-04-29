package com.moyeo.service.impl;

import com.moyeo.dao.PlanBoardDao;
import com.moyeo.dao.PlanPhotoDao;
import com.moyeo.service.PlanBoardService;
import com.moyeo.vo.Pin;
import com.moyeo.vo.PlanBoard;
import com.moyeo.vo.PlanPhoto;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class DefaultPlanBoardService implements PlanBoardService {
  private final PlanBoardDao planBoardDao;
  private final PlanPhotoDao planPhotoDao;
  @Override
  public List<PlanBoard> list(int recruitBoardId) {
    return planBoardDao.findAll(recruitBoardId);
  }

  @Override
  public PlanBoard get(int planBoardId) {
    PlanBoard planBoard = planBoardDao.findBy(planBoardId);
    return planBoard;
  }

  @Override
  public PlanBoard get(int planBoardId, String tripDate, double latitude, double longitude) {
    return planBoardDao.findByPlanBoard(planBoardId, tripDate, latitude, longitude);
  }

  @Transactional
  @Override
  public void add(PlanBoard planBoard) {
    planBoardDao.add(planBoard);

    if (planBoard.getPhotos() != null && planBoard.getPhotos().size() > 0) {
      for (PlanPhoto planPhoto : planBoard.getPhotos()) {
        planPhoto.setPlanBoardId(planBoard.getPlanBoardId());
      }

      planPhotoDao.addAll(planBoard.getPhotos());
    }
  }

  @Transactional
  @Override
  public int update(PlanBoard planBoard) {
    int count = planBoardDao.update(planBoard);
    planPhotoDao.deleteAll(planBoard.getPlanBoardId());

    if (planBoard.getPhotos() != null && planBoard.getPhotos().size() > 0) {
      for (PlanPhoto planPhoto : planBoard.getPhotos()) {
        planPhoto.setPlanBoardId(planBoard.getPlanBoardId());
      }
      planPhotoDao.addAll(planBoard.getPhotos());
    }

    return count;
  }

  @Transactional
  @Override
  public int delete(int planBoardId, int recruitBoardId) {
    planPhotoDao.deleteAll(planBoardId);
    return planBoardDao.delete(planBoardId, recruitBoardId);
  }

  @Override
  public PlanPhoto getPhoto(int planPhotoId) {
    return planPhotoDao.findByPlanPhotoId(planPhotoId);
  }

  @Override
  public List<PlanPhoto> getPhotos(int planBoardId) {
    return planPhotoDao.findAllByPlanBoardId(planBoardId);
  }

  @Override
  public int deletePlanPhoto(int planPhotoId) {
    return planPhotoDao.delete(planPhotoId);
  }

  @Override

  public List<PlanBoard> findByTripDate(@Param("tripDate") String tripDate, @Param("recruitBoardId")int recruitBoardId) {

//    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//    String stringifyTripDate = formatter.format(tripDate);
//
//    Map<String, Object> paramMap = new HashMap<>();
//    paramMap.put("recruitBoardId", recruitBoardId);
//    paramMap.put("tripDate", stringifyTripDate);

    return planBoardDao.findByTripDate(tripDate, recruitBoardId);
  }

  public List<Pin> pinList(int recruitBoardId, String tripDate) {
    return planBoardDao.findByPin(recruitBoardId, tripDate);
  }
}
