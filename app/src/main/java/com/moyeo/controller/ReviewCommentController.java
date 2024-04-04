package com.moyeo.controller;

import com.moyeo.service.ReviewCommentService;
import com.moyeo.vo.ReviewComment;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class ReviewCommentController {
  private final ReviewCommentService reviewCommentService;
  @PostMapping("add")
  public String commentAdd(int reviewBoardId, String reviewContent, HttpSession session) {

    ReviewComment reviewComment = ReviewComment.builder().reviewBoardId(reviewBoardId)
        .content(reviewContent).build();
    reviewCommentService.add(reviewComment);

    return "redirect:../review/view?reviewBoardId="+reviewBoardId;
  }

  @GetMapping("delete")
  public String delete(int reviewCommentId, int reviewBoardId) {
    reviewCommentService.delete(reviewCommentId);



    return "redirect:../review/view?reviewBoardId=" + reviewBoardId;
  }
}
