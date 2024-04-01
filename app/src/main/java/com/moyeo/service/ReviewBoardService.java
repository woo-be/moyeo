package com.moyeo.service;

import com.moyeo.vo.ReviewBoard;
import java.util.List;

public interface ReviewBoardService {

  void add(ReviewBoard reviewBoard);

List<ReviewBoard> list();
}
