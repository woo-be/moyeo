package com.moyeo.service.impl;

import com.moyeo.dao.RecruitBoardDao;
import com.moyeo.dao.RecruitCommentDao;
import com.moyeo.dao.RecruitPhotoDao;
import com.moyeo.service.RecruitBoardService;
import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitComment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DefaultRecruitBoardService implements RecruitBoardService {

  private static final Log log = LogFactory.getLog(DefaultRecruitBoardService.class);
  private final RecruitBoardDao recruitBoardDao;
  private final RecruitCommentDao recruitCommentDao;
  private final RecruitPhotoDao recruitPhotoDao;

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

  @Transactional
  @Override
  public int delete(int boardId) {
    recruitCommentDao.deleteAllCommentByRecruitBoardId(boardId);
//    recruitPhotoDao.deleteAllPhotoByRecruitBoardId(boardId);
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
  public int updateComment(RecruitComment recruitComment) {
    return recruitCommentDao.update(recruitComment);
  }

  @Override
  public int deleteComment(int commentId) {
    return recruitCommentDao.delete(commentId);
  }

  @Override
  public int countAll() {
    return recruitBoardDao.countAll();
  }

  @Override
  public void plusViews(int boardId) {
    recruitBoardDao.plusViews(boardId);
  }
}

