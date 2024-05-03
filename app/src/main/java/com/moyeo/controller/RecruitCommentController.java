package com.moyeo.controller;

import com.moyeo.service.AlarmService;
import com.moyeo.service.RecruitBoardService;
import com.moyeo.service.StorageService;
import com.moyeo.vo.Alarm;
import com.moyeo.vo.ErrorName;
import com.moyeo.vo.Member;
import com.moyeo.vo.MoyeoError;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/recruit/comment")
@SessionAttributes("recruitPhotos")
public class RecruitCommentController {
  private static final Log log = LogFactory.getLog(RecruitBoardController.class);
  private final RecruitBoardService recruitBoardService;
  private final StorageService storageService;
  private final AlarmService alarmService;
  private final String uploadDir = "recruit/";


  @PostMapping("add")
  @ResponseBody
  public String commentAdd(RecruitComment recruitComment, int recruitBoardId, HttpSession session) throws Exception{
    RecruitBoard recruitBoard = recruitBoardService.get(recruitBoardId);

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }

    recruitComment.setRecruitBoard(recruitBoard);
    recruitComment.setMember(loginUser);
    recruitBoardService.addComment(recruitComment);

    // 알림 읽음여부를 확인하기 위해 알림 id를 같이 넘겨준다
    Alarm alarm = Alarm.builder().memberId(recruitBoard.getWriter().getMemberId()).content(
        "<a href=\"/recruit/view?recruitBoardId="+
            recruitBoardId).build();
    alarmService.add(alarm);
    alarm.setContent(
        alarm.getContent()+
            "&alarmId="+
            alarm.getAlarmId()+
            "\">"+
            recruitBoardId+
            "번 모집</a>에 댓글을 등록했습니다."
    );
    alarmService.updateContent(alarm);

    return "1";
  }

  @PostMapping("update")
  public String commentUpdate(RecruitComment recruitComment, HttpSession session) throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }

    RecruitComment old = recruitBoardService.getComment(recruitComment.getRecruitCommentId());
    if (old.getMember().getMemberId() != loginUser.getMemberId()) {
      throw new MoyeoError(ErrorName.ACCESS_DENIED, "../../recruit/view?recruitBoardId=" + old.getRecruitBoard().getRecruitBoardId());
    }

    recruitComment.setRecruitBoard(old.getRecruitBoard());
    recruitComment.setMember(old.getMember());

    recruitBoardService.updateComment(recruitComment);
    return "redirect:../../recruit/view?recruitBoardId=" + recruitComment.getRecruitBoard().getRecruitBoardId();
  }

  @GetMapping("delete")
  public String commentDelete(int recruitCommentId, HttpSession session)throws Exception {
    RecruitComment recruitComment = recruitBoardService.getComment(recruitCommentId);
    int boardId = recruitComment.getRecruitBoard().getRecruitBoardId();

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null || loginUser.getMemberId() != recruitComment.getMember().getMemberId()) {
      throw new MoyeoError(ErrorName.ACCESS_DENIED, "../../recruit/view?recruitBoardId=" + boardId);
    }

    recruitBoardService.deleteComment(recruitCommentId);
    return "redirect:../../recruit/view?recruitBoardId=" + boardId;
  }
}
