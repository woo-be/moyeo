package com.moyeo.controller;

import com.moyeo.service.AlarmService;
import com.moyeo.service.RecruitBoardService;
import com.moyeo.service.RecruitMemberService;
import com.moyeo.service.RegionService;
import com.moyeo.service.StorageService;
import com.moyeo.service.ThemeService;
import com.moyeo.vo.ErrorName;
import com.moyeo.vo.Member;
import com.moyeo.vo.MoyeoError;
import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitMember;
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
import org.springframework.web.bind.annotation.RequestBody;
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
  private final RecruitMemberService recruitMemberService;
  private final StorageService storageService;
  private final AlarmService alarmService;
  private final String uploadDir = "recruit/";

  @Value("${ncp.ss.bucketname}")
  private String bucketName;

  @GetMapping("addForm")
  public void addForm(HttpSession session) throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }
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
      throw new MoyeoError("지역과 테마를 선택해주세요.", "addform");
    }



    // 현재 로그인한 사용자로 board 객체의 writer를 세팅함.
    Member loginUser = (Member) session.getAttribute("loginUser");
    board.setWriter(loginUser);

    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
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

    // 모집게시글을 작성하면 해당 게시글의 recruit_member에 작성자가 state=1로 들어간다.
    recruitMemberService.addWriter(board.getRecruitBoardId(), loginUser.getMemberId());

    return "redirect:list";
  }

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

    if (pageSize < 10 || pageSize > 20) {
      pageSize = 10;
    }
    if (pageNo < 1) {
      pageNo = 1;
    }

    int numOfPage = 1;
    int numOfRecord = recruitBoardService.countAll(regionId, themeId, filter, keyword);

    if (numOfRecord != 0) { // 해당하는 데이터가 하나라도 있다면,
      numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);
    }

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    /* 페이징 페이지 숫자 버튼 */
    int[] pageButtons; // 페이징 페이지 숫자 버튼

    if (numOfPage >= 5) { // a. 게시판 페이지가 5개 이상일 때,
      pageButtons = new int[5]; // 페이지 숫자 버튼의 개수를 5개로 함.

      if (pageNo <= 3) { // 1. 현재 페이지가 시작페이지에서 3페이지 이내의 페이지일 때,
        for (int i = 0; i < 5; i++) { // 숫자 버튼이 1부터 시작하도록 함.
          pageButtons[i] = i + 1;
        }
      } else if (pageNo >= (numOfPage - 2)) { // 2. 현재 페이지가 끝페이지에서 3페이지 이내의 페이지일 때,
        int temp = numOfPage;
        for (int i = 4; i >= 0; i--) { // 숫자 버튼이 끝페이지 -4 부터 시작하도록 함.
          pageButtons[i] = temp--;
        }
      } else { // 3. 그 외의 경우,
        int temp = pageNo - 2;
        for (int i = 0; i < 5; i++) { // 숫자 버튼의 가운데 버튼이 현재페이지를 가리키도록 함.
          pageButtons[i] = temp++;
        }
      }
    } else { // b. 게시판 페이지가 5개 미만일 때,
      pageButtons = new int[numOfPage]; // 페이지 숫자 버튼의 개수를 전체 페이지 개수로 함.
      for (int i = 0; i < numOfPage; i++) { // 숫자 버튼이 1부터 시작하도록 함.
        pageButtons[i] = i + 1;
      }
    }

    // list 메서드에 필요한 모든 값을 넘기고 mapper의 mybatis로 조건문 처리.
    model.addAttribute("list", recruitBoardService.list(pageNo, pageSize, regionId, themeId, filter, keyword));

    model.addAttribute("regionId", regionId);
    model.addAttribute("themeId", themeId);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
    model.addAttribute("keyword", keyword); // 검색어
    model.addAttribute("filter", filter); // 검색 필터(제목 | 내용 | 작성자)
    model.addAttribute("pageButtons", pageButtons); // 페이지 숫자 버튼
  }

  @GetMapping("view")
  public void view(int recruitBoardId, @RequestParam(required = false, defaultValue = "0") int alarmId, Model model, HttpSession session) throws Exception {

    // 유효한 번호인지 검사
    RecruitBoard recruitBoard = recruitBoardService.get(recruitBoardId);
    if (recruitBoard == null) {
      throw new MoyeoError(ErrorName.INVALID_NUMBER, "/recruit/list");
    }

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      loginUser = Member.builder().name("로그인해주세요.").build(); // memberId = 0

    } else { // 로그인 상태일 때, 신청 여부 파악하는 코드

      RecruitMember recruitMember = recruitMemberService.findBy(loginUser.getMemberId(),
          recruitBoardId);
      model.addAttribute("recruitMember", recruitMember);
    }
    if (alarmId != 0) {
      if (!alarmService.getStatus(alarmId)) {
        alarmService.update(alarmId);
      }
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

  @PostMapping("updateForm")
  public void updateForm(int recruitBoardId, Model model, HttpSession session) throws Exception {

    // 로그인한 상태인지 아닌지 검사.
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }

    // boardId로 게시글을 찾음.
    RecruitBoard board = recruitBoardService.get(recruitBoardId);

    // 해당 게시글의 작성자 정보와 로그인한 사용자의 정보가 일치하는지 검사.
    if (board.getWriter().getMemberId() != loginUser.getMemberId()) {
      throw new MoyeoError(ErrorName.ACCESS_DENIED, "/recruit/view?recruitBoardId=" + recruitBoardId);
    }

    // 해당 게시글을 "board"라는 이름으로 모델 객체에 저장.
    // regionId와 themeId도 별도로 저장.
    model.addAttribute("board", board);
    model.addAttribute("regionId", board.getRegion().getRegionId());
    model.addAttribute("themeId", board.getTheme().getThemeId());
  }

  @PostMapping("update")
  @ResponseBody
  public String update(
      @RequestBody RecruitBoard recruitBoard,
      HttpSession session,
      SessionStatus sessionStatus) throws Exception {

    // 로그인한 상태인지 아닌지 검사.
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }

    RecruitBoard old = recruitBoardService.get(recruitBoard.getRecruitBoardId());
    if (old == null) {
      throw new MoyeoError(ErrorName.INVALID_NUMBER, "/recruit/list");
    }

    // 지역 또는 테마를 선택하지 않으면 예외 발생.
    if (recruitBoard.getRegion().getRegionId() == 0 || recruitBoard.getTheme().getThemeId() == 0) {
      throw new MoyeoError(ErrorName.INVALID_NUMBER, "/recruit/view?recruitBoardId=" + recruitBoard.getRecruitBoardId());
    }

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
        if (recruitBoard.getContent().indexOf(recruitPhoto.getPhoto()) == -1) {
          storageService.delete(this.bucketName, this.uploadDir, recruitPhoto.getPhoto());
          recruitPhotos.remove(i);
        }
      }
    }

    if (recruitPhotos.size() > 0) {
      recruitBoard.setPhotos(recruitPhotos);
    }

    // DBMS의 정보를 해당 board 객체로 수정함.
    recruitBoardService.update(recruitBoard);

    // 게시글을 변경하는 과정에서 세션에 임시 보관한 첨부파일 목록 정보를 제거한다.
    sessionStatus.setComplete();

    return "1";
    //return "redirect:view?recruitBoardId=" + board.getRecruitBoardId();
  }


  @GetMapping("delete")
  @ResponseBody
  public String delete(int recruitBoardId, HttpSession session) throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null){
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }

    RecruitBoard recruitBoard = recruitBoardService.get(recruitBoardId);
    if (recruitBoard == null) {
      throw new MoyeoError(ErrorName.INVALID_NUMBER, "/recruit/list");
    }

    if (recruitBoard.getWriter().getMemberId() != loginUser.getMemberId()){
      throw new MoyeoError(ErrorName.ACCESS_DENIED, "/recruit/view?recruitBoardId=" + recruitBoardId);
    }

    List<RecruitPhoto> photos = recruitBoardService.getRecruitPhotos(recruitBoardId);

    recruitBoardService.delete(recruitBoardId);

    for (RecruitPhoto photo : photos) {
      storageService.delete(this.bucketName, this.uploadDir, photo.getPhoto());
    }

    return "1";
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

  @GetMapping("listByCurrent")
  public void findByCurrent(
      Model model,
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "0") int regionId,
      @RequestParam(defaultValue = "0") int themeId,
      @RequestParam(required = false) String filter, // 검색 필터(제목 | 내용 | 작성자)
      @RequestParam(required = false) String keyword // 검색어
  ) {

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
    model.addAttribute("list", recruitBoardService.findByCurrent(pageNo, pageSize, regionId, themeId, filter, keyword));

    model.addAttribute("regionId", regionId);
    model.addAttribute("themeId", themeId);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
    model.addAttribute("keyword", keyword); // 검색어
    model.addAttribute("filter", filter); // 검색 필터(제목 | 내용 | 작성자)
  }
}
