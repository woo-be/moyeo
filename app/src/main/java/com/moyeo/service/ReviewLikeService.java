package com.moyeo.service;

public interface ReviewLikeService {
  void add(int memberId, int reviewBoardId);
  int get(int memberId, int reviewBoardId);

}
