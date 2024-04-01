package com.moyeo.service.impl;

import com.moyeo.dao.ReviewBoardDao;
import com.moyeo.service.ReviewBoardService;
import com.moyeo.vo.ReviewBoard;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultReviewBoardService implements ReviewBoardService {

  private final ReviewBoardDao reviewBoardDao;

  @Override
  public List<ReviewBoard> list() {
    return reviewBoardDao.findAll();
  }
}
