package com.moyeo.service.impl;

import com.moyeo.controller.RecruitBoardController;
import com.moyeo.dao.RecruitBoardDao;
import com.moyeo.dao.RecruitCommentDao;
import com.moyeo.service.RecruitBoardService;
import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitComment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultRecruitBoardService implements RecruitBoardService {

  private static final Log log = LogFactory.getLog(DefaultRecruitBoardService.class);
  private final RecruitBoardDao recruitBoardDao;
  private final RecruitCommentDao recruitCommentDao;

  @Override
  public void add(RecruitBoard board) {
    recruitBoardDao.add(board);
  }

  @Override
  public List<RecruitBoard> list(int pageNo, int pageSize) {
    return recruitBoardDao.findAll(pageSize * (pageNo - 1), pageSize);
  }

  @Override
  public RecruitBoard get(int boardId) {
    RecruitBoard recruitBoard = recruitBoardDao.findBy(boardId);
    recruitBoard.setComments(recruitCommentDao.findAllByRecruitBoardId(boardId));

    return recruitBoard;
  }

  @Override
  public int update(RecruitBoard board) {
    return recruitBoardDao.update(board);
  }

  @Override
  public int delete(int boardId) {
    return recruitBoardDao.delete(boardId);
  }

  @Override
  public void addComment(RecruitComment comment) {
    recruitCommentDao.add(comment);
  }

  @Override
  public RecruitComment getComment(int commentId) {
    return recruitCommentDao.findBy(commentId);
  }

  @Override
  public int deleteComment(int commentId) {
    return recruitCommentDao.delete(commentId);
  }

  @Override
  public int countAll() {
    return recruitBoardDao.countAll();
  }
}
