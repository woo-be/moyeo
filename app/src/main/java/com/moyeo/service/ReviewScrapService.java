package com.moyeo.service;

public interface ReviewScrapService {

  void add(int memberId, int reviewBoardId);

  int get(int memberId, int reviewBoardId);

  int delete(int memberId, int reviewBoardId);
}
