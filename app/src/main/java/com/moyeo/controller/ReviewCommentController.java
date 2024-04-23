package com.moyeo.controller;

import com.moyeo.service.AlarmService;
import com.moyeo.service.ReviewCommentService;
import com.moyeo.vo.Alarm;
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

@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class ReviewCommentController {
  private final Log log = LogFactory.getLog(ReviewCommentController.class);
  private final ReviewCommentService reviewCommentService;
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
    alarmService.add(alarm);

    log.debug(alarm);
    alarm.setContent(
            alarm.getContent()+
            "&alarmId="+
            alarm.getAlarmId()+
            "\">"+
            reviewBoard.getReviewBoardId()+
            "번 후기</a>에 댓글을 등록했습니다."
        );
    alarmService.updateContent(alarm);

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
}
