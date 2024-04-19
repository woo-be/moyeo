package com.moyeo.service;

import com.moyeo.vo.ReviewComment;
import java.util.List;

public interface ReviewCommentService {

  void add(ReviewComment reviewComment);


  int delete(int reviewCommentId);

  int update(ReviewComment reviewComment);




}
