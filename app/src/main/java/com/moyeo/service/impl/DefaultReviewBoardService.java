package com.moyeo.service.impl;

import com.moyeo.dao.ReviewBoardDao;
import com.moyeo.dao.ReviewCommentDao;
import com.moyeo.dao.ReviewLikeDao;
import com.moyeo.dao.ReviewPhotoDao;
import com.moyeo.dao.ReviewScrapDao;
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

  private static final Log log = LogFactory.getLog(DefaultReviewBoardService.class);
  private final ReviewBoardDao reviewBoardDao;
  private final ReviewPhotoDao reviewPhotoDao;
  private final ReviewCommentDao reviewCommentDao;
  private final ReviewLikeDao reviewLikeDao;
  private final ReviewScrapDao reviewScrapDao;

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

  @Override
  public void increaseViews(int reviewBoardId) {
    reviewBoardDao.increaseViews(reviewBoardId);
  }


  @Override
  public List<ReviewBoard> list() {
    return reviewBoardDao.findAllNoPaging();
  }

  @Override
  public List<ReviewBoard> list(int pageNo, int pageSize) {
    return reviewBoardDao.findAll(pageSize * (pageNo - 1), pageSize);
  }

  @Override
  public List<ReviewBoard> list(int pageNo, int pageSize, int regionId) {
    List<ReviewBoard> list = reviewBoardDao.findAllByRegionId(pageSize * (pageNo - 1), pageSize,
        regionId);
    log.debug(list.getFirst());
    return list;
  }

  @Override
  public ReviewBoard get(int reviewBoardId) {
    ReviewBoard reviewBoard = reviewBoardDao.findBy(reviewBoardId);
    reviewBoard.setCommentList(reviewCommentDao.findAllByReviewId(reviewBoardId));
    return reviewBoard;
  }

  @Transactional
  @Override
  public int delete(int reviewBoardId) {
    reviewScrapDao.deleteAll(reviewBoardId);
    reviewLikeDao.deleteAll(reviewBoardId);
    reviewPhotoDao.deleteAll(reviewBoardId);
    reviewCommentDao.deleteAll(reviewBoardId);
    return reviewBoardDao.delete(reviewBoardId);
  }

  @Override
  public int countAll() {
    return reviewBoardDao.countAll();
  }

  @Override
  public int countAll(int regionId) {
    int c = reviewBoardDao.countAllByRegionId(regionId);
    return c;
  }

  @Override
  public int countPostedByMember(int memberId) {
    return reviewBoardDao.countPostedByMember(memberId);
  }

  @Transactional
  @Override
  public int update(ReviewBoard reviewBoard) {
    int count = reviewBoardDao.update(reviewBoard);
    reviewPhotoDao.deleteAll(reviewBoard.getReviewBoardId());

    if (reviewBoard.getPhotos() != null && reviewBoard.getPhotos().size() > 0) {
      for (ReviewPhoto reviewPhoto : reviewBoard.getPhotos()) {
        reviewPhoto.setReviewBoardId(reviewBoard.getReviewBoardId());
      }
      reviewPhotoDao.addAll(reviewBoard.getPhotos());
    }
    return count;
  }

  @Override
  public List<ReviewPhoto> getReviewPhotos(int reviewBoardId) {
    return reviewPhotoDao.findAllByReviewBoardId(reviewBoardId);
  }

  @Override
  public ReviewPhoto getReviewPhoto(int reviewPhotoId) {
    return reviewPhotoDao.findbyReviewPhotoId(reviewPhotoId);
  }

  @Override
  public int deleteReviewPhoto(int reviewPhotoId) {
    return reviewPhotoDao.delete(reviewPhotoId);
  }

  @Override
  public List<ReviewBoard> scrapList(int memberId, int pageNo, int pageSize) {
    return reviewBoardDao.scrapList(memberId, pageSize, (pageNo * pageSize) - pageSize);
  }

  @Override
  public int countScrapByMember(int memberId) {
    return reviewBoardDao.countScrapByMember(memberId);
  }

  @Override
  public List<ReviewBoard> reviewList(int memberId, int pageSize, int pageNo) {
    return reviewBoardDao.reviewList(memberId, pageSize, (pageNo * pageSize) - pageSize);
  }

}
