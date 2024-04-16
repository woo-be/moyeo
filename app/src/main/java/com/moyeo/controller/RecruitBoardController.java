package com.moyeo.controller;

import com.moyeo.service.RecruitBoardService;
import com.moyeo.service.RegionService;
import com.moyeo.service.StorageService;
import com.moyeo.service.ThemeService;
import com.moyeo.vo.Member;
import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitPhoto;
import com.moyeo.vo.Region;
import com.moyeo.vo.Theme;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
@RequestMapping("/recruit")
@SessionAttributes("recruitPhotos")
public class RecruitBoardController {

  private static final Log log = LogFactory.getLog(RecruitBoardController.class);
  private final RecruitBoardService recruitBoardService;
  private final RegionService regionService;
  private final ThemeService themeService;
  private final StorageService storageService;
  private final String uploadDir = "recruit/";

  @Value("${ncp.ss.bucketname}")
  private String bucketName;

  @GetMapping("list")
  public void list(
      Model model,
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "0") int regionId,
      @RequestParam(defaultValue = "0") int themeId,
      @RequestParam(required = false) String filter, // 검색 필터(제목 | 내용 | 작성자)
      @RequestParam(required = false) String keyword // 검색어
  ) {

    log.debug(filter);

    if (pageSize < 10 || pageSize > 20) {
      pageSize = 10;
    }
    if (pageNo < 1){
      pageNo = 1;
    }

    int numOfPage = 1;
    int numOfRecord = recruitBoardService.countAll(regionId, themeId, filter, keyword);
    numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    // list 메서드에 필요한 모든 값을 넘기고 mapper의 mybatis로 조건문 처리.
    model.addAttribute("list", recruitBoardService.list(pageNo, pageSize, regionId, themeId, filter, keyword));

    model.addAttribute("regionId", regionId);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
    model.addAttribute("keyword", keyword); // 검색어
    model.addAttribute("filter", filter); // 검색 필터(제목 | 내용 | 작성자)
  }

  @PostMapping("add")
  public String add(
      RecruitBoard board,
      int regionId,
      int themeId,
      HttpSession session,
      SessionStatus sessionStatus) throws Exception {

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

    // 게시글 등록할 때 삽입한 이미지 목록을 세션에서 가져온다.
    List<RecruitPhoto> recruitPhotos = (List<RecruitPhoto>) session.getAttribute("recruitPhotos");
    if (recruitPhotos == null) {
      recruitPhotos = new ArrayList<>();
    }

    for (int i = recruitPhotos.size() - 1; i >= 0; i--) {
      RecruitPhoto recruitPhoto = recruitPhotos.get(i);
      if (board.getContent().indexOf(recruitPhoto.getPhoto()) == -1) {
        // Object Storage에 업로드 한 파일 중에서 게시글 콘텐트에 포함되지 않은 것은 삭제한다.
        storageService.delete(this.bucketName, this.uploadDir, recruitPhoto.getPhoto());
        recruitPhotos.remove(i);
      }
    }
    if (recruitPhotos.size() > 0) {
      board.setPhotos(recruitPhotos);
    }

    // DBMS에 해당 게시물 업로드.
    recruitBoardService.add(board);

    // 게시글을 등록하는 과정에서 세션에 임시 보관한 첨부파일 목록 정보를 제거한다.
    sessionStatus.setComplete();

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
  public String update(
      RecruitBoard board,
      int themeId,
      int regionId,
      HttpSession session,
      SessionStatus sessionStatus) throws Exception {

    // 로그인한 상태인지 아닌지 검사.
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인 하시기 바랍니다.");
    }

    RecruitBoard old = recruitBoardService.get(board.getRecruitBoardId());
    if (old == null) {
      throw new Exception("유효하지 않은 번호입니다.");
    }

    // 지역 또는 테마를 선택하지 않으면 예외 발생.
    if (themeId == 0 || regionId == 0) {
      throw new Exception("지역과 테마를 선택해주세요.");
    }

    // board객체의 themeId와 regionId를 파라미터로 받은 themeId와 regionId로 설정함.
    board.setTheme(Theme.builder().themeId(themeId).build());
    board.setRegion(Region.builder().regionId(regionId).build());

    // 세션에서 사진 못가져오고있음
    // 게시글 등록할 때 삽입한 이미지 목록을 세션에서 가져온다.
    List<RecruitPhoto> recruitPhotos = (List<RecruitPhoto>) session.getAttribute("recruitPhotos");
    if (recruitPhotos == null) {
      recruitPhotos = new ArrayList<>();
    }

    if (old.getPhotos().size() > 0) {
      // 기존 게시글에 등록된 이미지 목록과 합친다.
      recruitPhotos.addAll(old.getPhotos());
    }

    if (recruitPhotos != null) {
      // Object Storage에 업로드 한 파일 중에서 게시글 콘텐트에 포함되지 않은 것은 삭제한다.
      for (int i = recruitPhotos.size() - 1; i >= 0; i--) {
        RecruitPhoto recruitPhoto = recruitPhotos.get(i);
        if (board.getContent().indexOf(recruitPhoto.getPhoto()) == -1) {
          storageService.delete(this.bucketName, this.uploadDir, recruitPhoto.getPhoto());
          recruitPhotos.remove(i);
        }
      }
    }

    if (recruitPhotos.size() > 0) {
      board.setPhotos(recruitPhotos);
    }

    // DBMS의 정보를 해당 board 객체로 수정함.
    recruitBoardService.update(board);

    // 게시글을 변경하는 과정에서 세션에 임시 보관한 첨부파일 목록 정보를 제거한다.
    sessionStatus.setComplete();

    return "redirect:list";
  }

  @GetMapping("view")
  public void view(int recruitBoardId, Model model,
      HttpSession session
  ) throws Exception {

    // 유효한 번호인지 검사
    RecruitBoard recruitBoard = recruitBoardService.get(recruitBoardId);
    if (recruitBoard == null) {
      throw new Exception("유효하지 않은 번호입니다.");
    }

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      loginUser = Member.builder().name("로그인해주세요.").build();
    }
    model.addAttribute("loginUser", loginUser);
    model.addAttribute("recruitboard", recruitBoard);
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
//    Member loginUser = (Member) session.getAttribute("loginUser");
//    if (loginUser == null){
//      throw new Exception("로그인해주세요");
//    }

    RecruitBoard recruitBoard = recruitBoardService.get(recruitBoardId);
    if (recruitBoard == null) {
      throw new Exception("유효하지 않은 번호입니다.");
    }

//    if (recruitBoard.getWriter().getMemberId() != loginUser.getMemberId()){
//      throw new Exception("권한이 없습니다.");
//    }

    List<RecruitPhoto> photos = recruitBoardService.getRecruitPhotos(recruitBoardId);

    recruitBoardService.delete(recruitBoardId);

    for (RecruitPhoto photo : photos) {
      storageService.delete(this.bucketName, this.uploadDir, photo.getPhoto());
    }

    return "redirect:list";
  }

  @PostMapping("file/upload")
  @ResponseBody
  public Object fileUpload(MultipartFile[] files, HttpSession session, Model model) throws Exception {
    // NCP Object Storage에 저장한 파일의 이미지 이름을 보관할 컬렉션을 준비한다.
    ArrayList<RecruitPhoto> recruitPhotos = new ArrayList<>();

    // 로그인 여부 검사
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      // 로그인 하지 않았으면 빈 목록 보냄
      return recruitPhotos;
    }

    // 클라이언트가 보낸 멀티파트 파일을 NCP Object Storage에 업로드
    for (MultipartFile file : files) {
      if (file.getSize() == 0) {
        continue;
      }
      String filename = storageService.upload(this.bucketName, this.uploadDir, file);
      recruitPhotos.add(RecruitPhoto.builder().photo(filename).build());
    }

    // 업로드한 파일 목록을 세션에 보관
    model.addAttribute("recruitPhotos", recruitPhotos);

    // 클라이언트에서 이미지 이름을 가지고 <img> 태그를 생성할 수 있도록 업로드한 파일의 이미지 정보 보냄
    return recruitPhotos;
  }
}
