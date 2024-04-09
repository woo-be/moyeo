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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
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
      int themeId,
      HttpSession session) throws Exception {

    // 지역 또는 테마를 선택하지 않으면 예외 발생.
    if (themeId == 0 || regionId == 0) {
      throw new Exception("지역과 테마를 선택해주세요.");
    }

    // 현재 로그인한 사용자로 board 객체의 writer를 세팅함.
    Member loginUser = (Member) session.getAttribute("loginUser");
    board.setWriter(loginUser);

    if (loginUser == null) {
      throw new Exception("로그인이 필요한 서비스입니다.");
    }

    // board 객체의 regionId와 themeId를 세팅함.
    board.setRegion(regionService.get(regionId));
    board.setTheme(themeService.get(themeId));

    // DBMS에 해당 게시물 업로드.
    recruitBoardService.add(board);

    return "redirect:list";
  }

  @GetMapping("addForm")
  public void addForm() throws Exception {
  }

  @PostMapping("updateForm")
  public void updateForm(int recruitBoardId, Model model, HttpSession session) throws Exception {

    // 로그인한 상태인지 아닌지 검사.
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인 하시기 바랍니다.");
    }

    // boardId로 게시글을 찾음.
    RecruitBoard board = recruitBoardService.get(recruitBoardId);

    // 해당 게시글의 작성자 정보와 로그인한 사용자의 정보가 일치하는지 검사.
    if (board.getWriter().getMemberId() != loginUser.getMemberId()) {
      throw new Exception("권한이 없습니다.");
    }

    // 해당 게시글을 "board"라는 이름으로 모델 객체에 저장.
    model.addAttribute("board", board);
  }

  @PostMapping("update")
  public String update(RecruitBoard board, int themeId, int regionId) throws Exception {

    // 지역 또는 테마를 선택하지 않으면 예외 발생.
    if (themeId == 0 || regionId == 0) {
      throw new Exception("지역과 테마를 선택해주세요.");
    }

    // board객체의 themeId와 regionId를 파라미터로 받은 themeId와 regionId로 설정함.
    board.setTheme(Theme.builder().themeId(themeId).build());
    board.setRegion(Region.builder().regionId(regionId).build());
    // DBMS의 정보를 해당 board 객체로 수정함.
    recruitBoardService.update(board);
    return "redirect:list";
  }

  @GetMapping("view")
  public void view(int recruitBoardId, Model model,
      HttpSession session
  ) throws Exception {

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

  // 조회수 증가시키는 요청핸들러?
  @GetMapping("viewCountUp")
  public String viewCountUp(int recruitBoardId,
      @CookieValue(required = false) String views, // 조회한 게시글 번호를 저장하는 쿠키
      HttpServletResponse res) {
    if (views == null || views.isEmpty()) { // 만약 쿠키가 없다면,
      Cookie cookie = new Cookie("views", "[" + recruitBoardId + "]");
      res.addCookie(cookie);
      recruitBoardService.plusViews(recruitBoardId);
    } else {
      if (!views.contains(String.valueOf(recruitBoardId))) { // 만약 쿠키가 있고, 쿠키에 해당 게시글 번호가 없다면,
        Cookie cookie = new Cookie("views", views + "[" + recruitBoardId + "]");
        res.addCookie(cookie);
        recruitBoardService.plusViews(recruitBoardId);
      }
    }
    // 만약 쿠키가 있고, 쿠키에 해당 게시글 번호가 있다면,
    // 조회수를 증가시키지 않고 바로 view로 redirect 한다.
    return "redirect:view?recruitBoardId=" + recruitBoardId;
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
