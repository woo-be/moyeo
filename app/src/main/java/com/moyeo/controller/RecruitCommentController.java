package com.moyeo.controller;

import com.moyeo.service.RecruitBoardService;
import com.moyeo.service.RegionService;
import com.moyeo.service.StorageService;
import com.moyeo.service.ThemeService;
import com.moyeo.vo.Member;
import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitComment;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/recruit/comment")
@SessionAttributes("recruitPhotos")
public class RecruitCommentController {
  private static final Log log = LogFactory.getLog(RecruitBoardController.class);
  private final RecruitBoardService recruitBoardService;
  private final StorageService storageService;
  private final String uploadDir = "recruit/";


  @PostMapping("add")
  public String commentAdd(RecruitComment recruitComment, int recruitBoardId, HttpSession session) {
    RecruitBoard recruitBoard = recruitBoardService.get(recruitBoardId);

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      log.debug("로그인해주세요.");  // .html에서 클릭했을 시 팝업 띄움
      return "redirect:../../recruit/view?recruitBoardId=" + recruitBoardId;
    }

    recruitComment.setRecruitBoard(recruitBoard);
    recruitComment.setMember(loginUser);
    recruitBoardService.addComment(recruitComment);
    return "redirect:../../recruit/view?recruitBoardId=" + recruitBoardId;
  }

  @PostMapping("update")
  public String commentUpdate(RecruitComment recruitComment, HttpSession session) throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인하시기 바랍니다!");
    }

    RecruitComment old = recruitBoardService.getComment(recruitComment.getRecruitCommentId());

    if (old.getMember().equals(loginUser)) {
      throw new Exception("권한이 없습니다.");
    }

    recruitComment.setRecruitBoard(old.getRecruitBoard());
    recruitComment.setMember(old.getMember());

    recruitBoardService.updateComment(recruitComment);
    return "redirect:../../recruit/view?recruitBoardId=" + recruitComment.getRecruitBoard().getRecruitBoardId();

  }

  @GetMapping("delete")
  public String commentDelete(int recruitCommentId, HttpSession session) {
    RecruitComment recruitComment = recruitBoardService.getComment(recruitCommentId);
    int boardId = recruitComment.getRecruitBoard().getRecruitBoardId();

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null || loginUser.getMemberId() != recruitComment.getMember().getMemberId()) {
      log.debug("권한이 없습니다."); // .html에서 클릭했을 시 팝업 띄움
      return "redirect:../../recruit/view?recruitBoardId=" + boardId;
    }

    recruitBoardService.deleteComment(recruitCommentId);
    return "redirect:../../recruit/view?recruitBoardId=" + boardId;
  }
}
