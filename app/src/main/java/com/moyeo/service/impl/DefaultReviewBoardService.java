package com.moyeo.service.impl;

import com.moyeo.dao.AlarmDao;
import com.moyeo.dao.ReviewBoardDao;
import com.moyeo.dao.ReviewCommentDao;
import com.moyeo.dao.ReviewLikeDao;
import com.moyeo.dao.ReviewPhotoDao;
import com.moyeo.dao.ReviewScrapDao;
import com.moyeo.service.ReviewBoardService;
import com.moyeo.vo.Alarm;
import com.moyeo.vo.ReviewBoard;
import com.moyeo.vo.ReviewComment;
import com.moyeo.vo.ReviewPhoto;
import java.util.ArrayList;
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
  private final AlarmDao alarmDao;

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
  public ReviewBoard get(int reviewBoardId) {
    ReviewBoard reviewBoard = reviewBoardDao.findBy(reviewBoardId);
    reviewBoard.setPhotos(reviewPhotoDao.findAllByReviewBoardId(reviewBoardId));
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

    List<Alarm> alarmList = alarmDao.listAll();
    // 관련 없는 알림을 제거해야 하는 리스트
    List<Alarm> removeAlarm = new ArrayList<>();
    // 관련 알림 내용
    String removeStr = reviewBoardId + "번 후기";
    // 관련 없는 알림 리스트
    for (Alarm alarm : alarmList) {
      if (!alarm.getContent().contains(removeStr)) {
        removeAlarm.add(alarm);
      }
    }
    // 관련 없는 알림 리스트를 제거
    alarmList.removeAll(removeAlarm);
    // 후기 삭제 할 때 삭제 해야하는 알림 제거
    for (Alarm alarm : alarmList) {
      alarmDao.delete(alarm.getAlarmId());
    }

    return reviewBoardDao.delete(reviewBoardId);
  }

  @Override
  public int countAll(int regionId, int themeId,String filter,String keyword) {
    return reviewBoardDao.countAll(regionId, themeId, filter, keyword);
  }


  @Override
  public int countPostedByMember(int memberId) {
    return reviewBoardDao.countPostedByMember(memberId);
  }

  @Transactional
  @Override
  public int update(ReviewBoard reviewBoard) {
    int count = reviewBoardDao.update(reviewBoard);
//    reviewPhotoDao.deleteAll(reviewBoard.getReviewBoardId());

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
    return reviewPhotoDao.findByReviewPhotoId(reviewPhotoId);
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

  @Override
  public List<ReviewBoard> list(int pageNo, int pageSize, int regionId, int themeId, String filter,
      String keyword) {
    return reviewBoardDao.findAll(pageSize * (pageNo - 1), pageSize, regionId, themeId, filter, keyword);
  }

  @Override
  public List<ReviewBoard> findByCreatedDate(int pageNo, int pageSize, int regionId,int themeId, String filter, String keyword) {
    return reviewBoardDao.findByCreatedDate(pageSize * (pageNo - 1), pageSize, regionId, themeId, filter, keyword);
  }

  @Override
  public List<ReviewBoard> findByCreatedDateByLimit3() {
    return reviewBoardDao.findByCreatedDateByLimit3();
  }

  @Override
  public List<ReviewBoard> findByLikeCountByLimit3() {
    return reviewBoardDao.findByLikeCountByLimit3();
  }

  @Override
  public List<ReviewBoard> findByLikeCount(int pageNo, int pageSize, int regionId,int themeId, String filter, String keyword) {
    return reviewBoardDao.findByLikeCount(pageSize * (pageNo - 1), pageSize, regionId, themeId, filter, keyword);
  }

  @Override
  public List<ReviewBoard> findByViewsByLimit3() {
    return reviewBoardDao.findByViewsByLimit3();
  }

  @Override
  public List<ReviewBoard> findByViews(int pageNo, int pageSize, int regionId, int themeId,
      String filter, String keyword) {
    return reviewBoardDao.findByViews(pageSize * (pageNo - 1), pageSize, regionId, themeId, filter, keyword);
  }

  @Override
  public ReviewComment getComment(int commentId) {
    return reviewCommentDao.findBy(commentId);
  }

  @Override
  public void addComment(ReviewComment comment) {
    reviewCommentDao.add(comment);
  }

  @Override
  public int updateComment(ReviewComment reviewComment) {
    return reviewCommentDao.reviewCommentUpdate(reviewComment);
  }

  @Override
  public int deleteComment(int commentId) {
    return reviewCommentDao.delete(commentId);
  }

  @Override
  public List<ReviewBoard> findByCreatedDate2() {
    return reviewBoardDao.findByCreatedDate2();
  }
}
