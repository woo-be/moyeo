package com.moyeo.service.impl;

import com.moyeo.dao.PlanBoardDao;
import com.moyeo.dao.PlanPhotoDao;
import com.moyeo.service.PlanBoardService;
import com.moyeo.vo.PlanBoard;
import com.moyeo.vo.PlanPhoto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultPlanBoardService implements PlanBoardService {
  private final PlanBoardDao planBoardDao;
  private final PlanPhotoDao planPhotoDao;
  @Override
  public List<PlanBoard> list() {
    return planBoardDao.findAll();
  }

  @Override
  public PlanBoard get(int planBoardId) {
    PlanBoard planBoard = planBoardDao.findBy(planBoardId);
    return planBoard;
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

}
