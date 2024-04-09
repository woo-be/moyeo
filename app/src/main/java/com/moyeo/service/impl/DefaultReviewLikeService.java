package com.moyeo.service.impl;

import com.moyeo.dao.ReviewLikeDao;
import com.moyeo.service.ReviewLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultReviewLikeService implements ReviewLikeService {
  private final ReviewLikeDao reviewLikeDao;

  @Override
  public void add(int memberId, int reviewBoardId) {
      reviewLikeDao.add(memberId, reviewBoardId);
  }
}
