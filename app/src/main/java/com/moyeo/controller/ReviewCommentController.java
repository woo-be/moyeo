package com.moyeo.controller;

import com.moyeo.service.AlarmService;
import com.moyeo.service.ReviewBoardService;
import com.moyeo.service.ReviewCommentService;
import com.moyeo.vo.Alarm;
import com.moyeo.vo.ErrorName;
import com.moyeo.vo.Member;
import com.moyeo.vo.MoyeoError;
import com.moyeo.vo.ReviewBoard;
import com.moyeo.vo.ReviewComment;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
@RequestMapping("/review/comment")
public class ReviewCommentController {
  private final Log log = LogFactory.getLog(ReviewCommentController.class);
  private final ReviewCommentService reviewCommentService;
  private final ReviewBoardService reviewBoardService;
  private final AlarmService alarmService;
  @PostMapping("add")
  public String commentAdd(ReviewBoard reviewBoard, @RequestParam("reviewContent") String reviewContent, HttpSession session)
      throws MoyeoError {

    Member loginUser = (Member) session.getAttribute("loginUser");

    if(loginUser == null){
      throw new MoyeoError("로그인 해주세요", "/auth/form");
    }

    ReviewComment reviewComment = ReviewComment.builder().reviewBoardId(reviewBoard.getReviewBoardId())
        .content(reviewContent).commentMember(loginUser).build();

    // 알림 읽음여부를 확인하기 위해 알림 id를 같이 넘겨준다
    Alarm alarm = Alarm.builder().memberId(reviewBoard.getMemberId()).content(
            "<a href=\"/review/view?reviewBoardId="+
            reviewBoard.getReviewBoardId()).build();

    reviewCommentService.add(reviewComment);

    alarmService.reviewCommentAdd(alarm, reviewBoard.getReviewBoardId());

    return "redirect:../review/view?reviewBoardId="+reviewBoard.getReviewBoardId();
  }


  @GetMapping("delete")
  public String delete(int reviewCommentId, int reviewBoardId) {
    reviewCommentService.delete(reviewCommentId);

    return "redirect:../review/view?reviewBoardId=" + reviewBoardId;
  }

  @PostMapping("update")
  public String commentUpdate(ReviewComment reviewComment) {

    log.debug(String.format("게시글 댓글 내용 : %d %d %s\n",
        reviewComment.getReviewBoardId(),
        reviewComment.getReviewCommentId(),
        reviewComment.getContent()));
    reviewCommentService.update(reviewComment);

    return "redirect:../review/view?reviewBoardId="+reviewComment.getReviewBoardId();

  }

  @PostMapping("add2")
  @ResponseBody
  public String commentAdd2(ReviewComment reviewComment, int reviewBoardId, HttpSession session) throws Exception{
    ReviewBoard reviewBoard = reviewBoardService.get(reviewBoardId);

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }

    reviewComment.setReviewBoard(reviewBoard);
    reviewComment.setCommentMember(loginUser);
    reviewBoardService.addComment(reviewComment);
    log.debug(String.format("QQQQQQQQQQQQQQQQQQQQQQQ%s",reviewComment));

    // 알림 읽음여부를 확인하기 위해 알림 id를 같이 넘겨준다
    Alarm alarm = Alarm.builder().memberId(reviewBoard.getWriter().getMemberId()).content(
        "<a href=\"/review/view?reviewBoardId="+
            reviewBoardId).build();
    alarmService.reviewCommentAdd(alarm, reviewBoardId);

    return "1";
  }

  @PostMapping("update2")
  public String commentUpdate2(ReviewComment reviewComment, HttpSession session) throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }

    ReviewComment old = reviewBoardService.getComment(reviewComment.getReviewCommentId());
    if (old.getCommentMember().getMemberId() != loginUser.getMemberId()) {
      throw new MoyeoError(ErrorName.ACCESS_DENIED, "../../review/view?reviewBoardId=" + old.getReviewBoard().getReviewBoardId());
    }

    reviewComment.setReviewBoard(old.getReviewBoard());
    reviewComment.setCommentMember(old.getCommentMember());

    reviewBoardService.updateComment(reviewComment);
    return "redirect:../../review/view?reviewBoardId=" + reviewComment.getReviewBoard().getReviewBoardId();
  }

  @GetMapping("delete2")
  public String commentDelete2(int reviewCommentId, HttpSession session)throws Exception {
    ReviewComment reviewComment = reviewBoardService.getComment(reviewCommentId);
    int boardId = reviewComment.getReviewBoard().getReviewBoardId();

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null || loginUser.getMemberId() != reviewComment.getCommentMember().getMemberId()) {
      throw new MoyeoError(ErrorName.ACCESS_DENIED, "../../review/view?reviewBoardId=" + boardId);
    }

    reviewBoardService.deleteComment(reviewCommentId);
    return "redirect:../../review/view?reviewBoardId=" + boardId;
  }
}
