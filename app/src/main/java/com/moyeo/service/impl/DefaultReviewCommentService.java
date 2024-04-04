package com.moyeo.service.impl;

import com.moyeo.dao.ReviewCommentDao;
import com.moyeo.service.ReviewCommentService;
import com.moyeo.vo.ReviewComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultReviewCommentService implements ReviewCommentService {
private final ReviewCommentDao reviewCommentDao;
  @Override
  public void add(ReviewComment reviewComment) {
    reviewCommentDao.reviewCommentPost(reviewComment);
  }

  @Override
  public int update(ReviewComment reviewComment) {
    return reviewCommentDao.reviewCommentUpdate(reviewComment);
  }
}
