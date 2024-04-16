package com.moyeo.controller;

import com.moyeo.service.ReviewCommentService;
import com.moyeo.vo.Member;
import com.moyeo.vo.ReviewComment;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class ReviewCommentController {
  private final Log log = LogFactory.getLog(ReviewCommentController.class);
  private final ReviewCommentService reviewCommentService;
  @PostMapping("add")
  public String commentAdd(int reviewBoardId, String reviewContent, HttpSession session) {

    Member loginUser = (Member) session.getAttribute("loginUser");

    if(loginUser == null){
      session.setAttribute("message", "로그인 해주세요");
      session.setAttribute("replaceUrl", "/auth/form");
    }

    ReviewComment reviewComment = ReviewComment.builder().reviewBoardId(reviewBoardId)
        .content(reviewContent).commentMember(loginUser).build();

    reviewCommentService.add(reviewComment);

    return "redirect:../review/view?reviewBoardId="+reviewBoardId;
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
