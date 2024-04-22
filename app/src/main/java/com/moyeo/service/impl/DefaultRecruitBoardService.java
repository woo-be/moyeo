package com.moyeo.service.impl;

import com.moyeo.dao.RecruitBoardDao;
import com.moyeo.dao.RecruitCommentDao;
import com.moyeo.dao.RecruitPhotoDao;
import com.moyeo.service.RecruitBoardService;
import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitComment;
import com.moyeo.vo.RecruitMember;
import com.moyeo.vo.RecruitPhoto;
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

  @Transactional
  @Override
  public void add(RecruitBoard board) {
    recruitBoardDao.add(board);
    if (board.getPhotos() != null && board.getPhotos().size() > 0) {
      for (RecruitPhoto photo : board.getPhotos()) {
        photo.setRecruitBoard(board);
      }
      recruitPhotoDao.addAll(board.getPhotos());
    }
  }

  @Override
  public List<RecruitBoard> list(int pageNo, int pageSize, int regionId, int themeId, String filter, String keyword) {
    return recruitBoardDao.findAll(pageSize * (pageNo - 1), pageSize, regionId, themeId, filter, keyword);
  }

  @Override
  public List<RecruitBoard> mypost(int memberId) {
    return recruitBoardDao.findByMemberId(memberId);
  }

  @Override
  public List<RecruitBoard> myrequest(int memberId) {
    return recruitBoardDao.findReqByMemberId(memberId);
  }

  @Override
  public List<RecruitBoard> teamlist(int memberId) {
    return recruitBoardDao.findMyTeamByMemberId(memberId);
  }

  @Override
  public RecruitBoard get(int boardId) {
    RecruitBoard recruitBoard = recruitBoardDao.findBy(boardId);
    recruitBoard.setComments(recruitCommentDao.findAllByRecruitBoardId(boardId));
    if (recruitBoard.getComments() != null && recruitBoard.getComments().size()>0 ){
      log.debug("photoId: "+ recruitBoard.getComments().getFirst().getMember().getPhoto());
    }
    recruitBoard.setPhotos(recruitPhotoDao.findAllByBoardId(boardId));

    return recruitBoard;
  }

  @Transactional
  @Override
  public int update(RecruitBoard board) {
    int count = recruitBoardDao.update(board);
    recruitPhotoDao.deleteAllPhotoByRecruitBoardId(board.getRecruitBoardId());

    if (board.getPhotos() != null && board.getPhotos().size() > 0) {
      for (RecruitPhoto recruitPhoto : board.getPhotos()) {
        recruitPhoto.setRecruitBoard(board);
      }
      recruitPhotoDao.addAll(board.getPhotos());
    }
    return count;
  }

  @Transactional
  @Override
  public int delete(int boardId) {
    recruitCommentDao.deleteAllCommentByRecruitBoardId(boardId);
    recruitPhotoDao.deleteAllPhotoByRecruitBoardId(boardId);

    return recruitBoardDao.delete(boardId);
  }

  @Override
  public List<RecruitPhoto> getRecruitPhotos(int recruitBoardId) {
    return recruitPhotoDao.findAllByBoardId(recruitBoardId);
  }

  @Override
  public RecruitPhoto getRecruitPhoto(int recruitPhotoId) {
    return recruitPhotoDao.findById(recruitPhotoId);
  }

  @Override
  public int deleteRecruitPhoto(int recruitPhotoId) {
    return recruitPhotoDao.delete(recruitPhotoId);
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

  public int countAll(int regionId, int themeId, String filter, String keyword) {
    return recruitBoardDao.countAll(regionId, themeId, filter, keyword);
  }

  @Override
  public void plusViews(int boardId) { // 조회수 증가
    recruitBoardDao.plusViews(boardId);
  }

}

