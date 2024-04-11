package com.moyeo.service.impl;

import com.moyeo.dao.ReviewScrapDao;
import com.moyeo.service.ReviewScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultReviewScrapService implements ReviewScrapService {

  private final ReviewScrapDao reviewScrapDao;

  @Override
  public void add(int memberId, int reviewBoardId) {
    reviewScrapDao.add(memberId, reviewBoardId);
  }
}
