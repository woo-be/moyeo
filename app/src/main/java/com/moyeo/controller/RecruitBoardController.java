package com.moyeo.controller;

import com.moyeo.service.RecruitBoardService;
import com.moyeo.service.RegionService;
import com.moyeo.service.StorageService;
import com.moyeo.service.ThemeService;
import com.moyeo.vo.Member;
import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.Theme;
import com.moyeo.vo.RecruitComment;
import com.moyeo.vo.Region;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/recruit")
public class RecruitBoardController {

  private static final Log log = LogFactory.getLog(RecruitBoardController.class);
  private final RecruitBoardService recruitBoardService;
  private final RegionService regionService;
  private final ThemeService themeService;
  private final StorageService storageService;

  @GetMapping("list")
  public void list(
      Model model,
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "10") int pageSize) {

    if (pageSize < 10 || pageSize > 20) {
      pageSize = 10;
    }
    if (pageNo < 1){
      pageNo = 1;
    }

    int numOfRecord = recruitBoardService.countAll();
    int numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    model.addAttribute("list", recruitBoardService.list(pageNo, pageSize));

    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
  }

  @PostMapping("add")
  public String add(
      RecruitBoard board,
      int regionId,
      int themeId) throws Exception {
    if (themeId == 0 || regionId == 0) {
      throw new Exception("지역과 테마를 선택해주세요.");
    }
    board.setRegion(regionService.get(regionId));
    board.setTheme(themeService.get(themeId));

    // 임시 멤버 객체, 세션에서 받아오도록 해야 함.
    Member loginUser = Member.builder()
        .memberId(6)
        .name("비트").build();

    board.setWriter(loginUser);

    recruitBoardService.add(board);

    return "redirect:list";
  }

  @GetMapping("addForm")
  public String addForm() throws Exception {
    return "recruit/addForm";
  }

  @PostMapping("updateForm")
  public String updateForm(int recruitBoardId, Model model) throws Exception {
    RecruitBoard board = recruitBoardService.get(recruitBoardId);
    model.addAttribute("board", board);
    return "recruit/updateForm";
  }

  @PostMapping("update")
  public String update(RecruitBoard board, int themeId, int regionId) throws Exception {
    if (themeId == 0 || regionId == 0) {
      throw new Exception("지역과 테마를 선택해주세요.");
    }
    board.setTheme(Theme.builder().themeId(themeId).build());
    board.setRegion(Region.builder().regionId(regionId).build());
    recruitBoardService.update(board);
    return "redirect:list";
  }

  @GetMapping("view")
  public void view(int recruitBoardId, Model model, HttpSession session) throws Exception {

    RecruitBoard recruitBoard = recruitBoardService.get(recruitBoardId);
    if (recruitBoard == null) {
      throw new Exception("유효하지 않은 번호입니다.");
    }
    model.addAttribute("recruitboard", recruitBoard);

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      loginUser = Member.builder().name("로그인해주세요.").build();
    }
    model.addAttribute("loginUser", loginUser);
  }

  @GetMapping("delete")
  public String delete(int recruitBoardId, HttpSession session) throws Exception {
    RecruitBoard recruitBoard = recruitBoardService.get(recruitBoardId);
    if (recruitBoard == null) {
      throw new Exception("유효하지 않은 번호입니다.");
    }

//    Member loginUser = (Member) session.getAttribute("loginUser");
//    if (loginUser == null || recruitBoard.getWriter().getMemberId() != loginUser.getMemberId()){
//      throw new Exception("권한이 없습니다.");
//    }

    // YJ_TODO: photo 삭제 코드 추가해야됨
    recruitBoardService.delete(recruitBoardId);

    return "redirect:list";
  }

  @PostMapping("comment/add")
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

  @GetMapping("comment/delete")
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
