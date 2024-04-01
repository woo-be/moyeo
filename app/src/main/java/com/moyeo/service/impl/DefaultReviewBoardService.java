package com.moyeo.service.impl;

import com.moyeo.controller.ReviewBoardController;
import com.moyeo.dao.ReviewBoardDao;
import com.moyeo.dao.ReviewPhotoDao;
import com.moyeo.service.ReviewBoardService;
import com.moyeo.vo.ReviewBoard;
import com.moyeo.vo.ReviewPhoto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DefaultReviewBoardService implements ReviewBoardService {

  private final ReviewBoardDao reviewBoardDao;
  private final ReviewPhotoDao reviewPhotoDao;

  @Transactional
  @Override
  public void add(ReviewBoard reviewBoard) {
    reviewBoardDao.add(reviewBoard);

    if (reviewBoard.getPhotos() != null && reviewBoard.getPhotos().size() > 0) {
      for (ReviewPhoto reviewPhoto : reviewBoard.getPhotos()) {
        reviewPhoto.setReviewBoardId(reviewBoard.getReviewBoardId());
      }

      reviewPhotoDao.addAll(reviewBoard.getPhotos());
    }
  }
  private static final Log log = LogFactory.getLog(DefaultReviewBoardService.class);

  @Override
  public List<ReviewBoard> list() {
    return reviewBoardDao.findAll();
  }

  @Override
  public ReviewBoard get(int reviewBoardId) {
    return reviewBoardDao.findBy(reviewBoardId);
  }
}
