package com.moyeo.controller;

import com.moyeo.service.AlarmService;
import com.moyeo.service.RegionService;
import com.moyeo.service.ReviewBoardService;
import com.moyeo.service.ReviewCommentService;
import com.moyeo.service.StorageService;
import com.moyeo.service.ThemeService;
import com.moyeo.vo.Alarm;
import com.moyeo.vo.Member;
import com.moyeo.vo.MoyeoError;
import com.moyeo.vo.ReviewBoard;
import com.moyeo.vo.ReviewPhoto;
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
@RequestMapping("/review")
@SessionAttributes("reviewPhotos")
public class ReviewBoardController {

  private static final Log log = LogFactory.getLog(ReviewBoardController.class);
  private final ReviewBoardService reviewBoardService;
  private final StorageService storageService;
  private final ReviewCommentService reviewCommentService;
  private final RegionService regionService;
  private final ThemeService themeService;
  private final AlarmService alarmService;
  private final String uploadDir = "review/";

  @Value("${ncp.ss.bucketname}")
  private String bucketName;

  @GetMapping("form")
  public void form(Model model, HttpSession session) throws Exception {
    model.addAttribute("regionId", 0);
    model.addAttribute("themeId", 0);

    Member loginUser = (Member) session.getAttribute("loginUser");
    log.debug(loginUser);
    if (loginUser == null) {
      throw new MoyeoError("로그인 해주세요", "/auth/form");
    }
  }

  @PostMapping("add")
  public String add(
      ReviewBoard reviewBoard,
      HttpSession session,
      SessionStatus sessionStatus,
      Model model) throws Exception {

    Member loginUser = (Member) session.getAttribute("loginUser");

    reviewBoard.setWriter(loginUser);
    Member writer = (Member) session.getAttribute("loginUser");
    reviewBoard.setWriter(writer);
    List<ReviewPhoto> reviewPhotos = (List<ReviewPhoto>) session.getAttribute("reviewPhotos");

    log.debug(reviewBoard.getThemeList());
    log.debug(reviewBoard.getLatitude());
    log.debug(reviewBoard.getLongitude());
    log.debug(String.format("%s==================================\n", reviewBoard.getAddress()));

    if (reviewPhotos != null) {
      for (int i = reviewPhotos.size() - 1; i >= 0; i--) {
        ReviewPhoto reviewPhoto = reviewPhotos.get(i);
        if (reviewBoard.getContent().indexOf(reviewPhoto.getPhoto()) == -1) {
          storageService.delete(this.bucketName, this.uploadDir, reviewPhoto.getPhoto());
          log.debug(String.format("%s 파일 삭제!", reviewPhoto.getPhoto()));
          reviewPhotos.remove(i);
        }
      }
      if (reviewPhotos.size() > 0) {
        reviewBoard.setPhotos(reviewPhotos);
      }
    }

    reviewBoardService.add(reviewBoard);

    sessionStatus.setComplete();

    return "redirect:view?reviewBoardId=" + reviewBoard.getReviewBoardId();
  }


  @GetMapping("list")
  public void list(
      @RequestParam(required = false) String filter,
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "6") int pageSize,
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "0") int regionId, // 원하는 지역 id를 요청
      @RequestParam(defaultValue = "0") int themeId, // 원하는 테마 id를 요청
      Model model) throws Exception {
    if (pageSize < 3 || pageSize > 20) {
      pageSize = 3;
    }
    if (pageNo < 1) {
      pageNo = 1;
    }

    int numOfPage = 1;
    int numOfRecord = reviewBoardService.countAll(regionId, themeId, filter, keyword);
    numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);

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
    model.addAttribute("list",
        reviewBoardService.list(pageNo, pageSize, regionId, themeId, filter, keyword));

    model.addAttribute("filter", filter);
    model.addAttribute("keyword", keyword);
    model.addAttribute("regionId", regionId);
    model.addAttribute("themeId", themeId);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
    model.addAttribute("pageButtons", pageButtons); // 페이지 숫자 버튼
  }

  @GetMapping("view")
  public void reviewBoardGet(int reviewBoardId,
      @RequestParam(required = false, defaultValue = "0") int alarmId,
      Model model) {
    ReviewBoard reviewBoard = reviewBoardService.get(reviewBoardId);
    if (reviewBoard.getAddress() == null) {
      reviewBoard.setAddress("서울특별시 용산구 한강대로 405");
    }

    if (alarmId != 0) {
      if (!alarmService.getStatus(alarmId)) {
        alarmService.update(alarmId);
      }
    }

    model.addAttribute("reviewBoard", reviewBoard);
    model.addAttribute("addr", reviewBoard.getAddress());
  }

  @GetMapping("views")
  public String viewsCount(
      @CookieValue(required = false) String views,
      HttpServletResponse response,
      int reviewBoardId) {
    if (views == null) {
      Cookie cookie = new Cookie("views", ("[" + reviewBoardId + "]"));
      response.addCookie(cookie);
      // view를 요청 하면 후기 테이블에 views 컬럼에 1씩 증가한다
      reviewBoardService.increaseViews(reviewBoardId);
    } else if (!views.contains(String.valueOf(reviewBoardId))) {
      Cookie cookie = new Cookie("views", views + ("[" + reviewBoardId + "]"));
      response.addCookie(cookie);
      // view를 요청 하면 후기 테이블에 views 컬럼에 1씩 증가한다
      reviewBoardService.increaseViews(reviewBoardId);
    }

    return "redirect:view?reviewBoardId=" + reviewBoardId;
  }

  @GetMapping("delete")
  @ResponseBody
  public String delete(
      int reviewBoardId,
      HttpSession session
  ) throws Exception {

    ReviewBoard reviewBoard = reviewBoardService.get(reviewBoardId);

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      return "-1";
    } else {
      if (reviewBoard == null) {
        return "-2";
      } else if (reviewBoard.getWriter().getMemberId() != loginUser.getMemberId()) {
        return "-3";
      }
    }

    List<Alarm> alarmList = alarmService.listAll();
    // 관련 없는 알림을 제거해야 하는 리스트
    List<Alarm> removeAlarm = new ArrayList<>();
    // 관련 알림 내용
    String removeStr = reviewBoardId + "번 후기에";
    // 관련 없는 알림 리스트
    for (Alarm alarm : alarmList) {
      if (!alarm.getContent().contains(removeStr)) {
        removeAlarm.add(alarm);
      }
    }
    // 관련 없는 알림 리스트를 제거
    alarmList.removeAll(removeAlarm);
    // 후기 삭제 할 때 삭제 해야하는 알림 제거
    for (Alarm alarm : alarmList) {
      alarmService.delete(alarm.getAlarmId());
    }

    reviewBoardService.delete(reviewBoardId);
    return "/review/list";
  }

  @PostMapping("update")
  public String update(
      ReviewBoard reviewBoard,
      HttpSession session,
      Model model,
      SessionStatus sessionStatus) throws Exception {

    model.addAttribute("updateReviewBoard", reviewBoard);

    ReviewBoard old = reviewBoardService.get(reviewBoard.getReviewBoardId());
    log.debug(String.format("%s~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", old.getPhotos().getFirst().getPhoto()));

    List<ReviewPhoto> reviewPhotos = (List<ReviewPhoto>) session.getAttribute("reviewPhotos");
    if (reviewPhotos == null) {
      reviewPhotos = new ArrayList<>();
    }

    if (old.getPhotos().getFirst().getPhoto() != null) {
      reviewPhotos.addAll(old.getPhotos());
    }

    if (reviewPhotos != null) {
      for (int i = reviewPhotos.size() - 1; i >= 0; i--) {
        ReviewPhoto reviewPhoto = reviewPhotos.get(i);
        if (reviewBoard.getContent().indexOf(reviewPhoto.getPhoto()) == -1) {
          storageService.delete(this.bucketName, this.uploadDir, reviewPhoto.getPhoto());
          log.debug(String.format("%s 파일 삭제!", reviewPhoto.getPhoto()));
          reviewPhotos.remove(i);
        }
      }

      if (reviewPhotos.size() > 0) {
        reviewBoard.setPhotos(reviewPhotos);
      }
    }

    reviewBoardService.update(reviewBoard);

    sessionStatus.setComplete();

    return "redirect:view?reviewBoardId=" + reviewBoard.getReviewBoardId();
  }

  @PostMapping("updateForm")
  public void updateForm(ReviewBoard reviewBoard, Model model, HttpSession session)
      throws MoyeoError {
    model.addAttribute("updateReviewBoard", reviewBoard);

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError("로그인하시기 바랍니다!", "/auth/form");
    }

    ReviewBoard old = reviewBoardService.get(reviewBoard.getReviewBoardId());
    if (old == null) {
      throw new MoyeoError("번호가 유효하지 않습니다.", "/home");
    } else if (old.getWriter().getMemberId() != loginUser.getMemberId()) {
      log.debug(old.getWriter());
      throw new MoyeoError("권한이 없습니다.", "view?reviewBoardId=" + reviewBoard.getReviewBoardId());
    }

    model.addAttribute("regionId", reviewBoard.getRegionId());
    model.addAttribute("themeId", reviewBoard.getThemeId());
  }


  @PostMapping("photo/upload")
  @ResponseBody
  public Object photoUpload(MultipartFile[] photos, HttpSession session, Model model)
      throws Exception {

    ArrayList<ReviewPhoto> reviewPhotos = new ArrayList<>();

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      return reviewPhotos;
    }

    for (MultipartFile photo : photos) {
      if (photo.getSize() == 0) {
        continue;
      }
      String photoName = storageService.upload(this.bucketName, this.uploadDir, photo);
      reviewPhotos.add(ReviewPhoto.builder().photo(photoName).build());
    }

    model.addAttribute("reviewPhotos", reviewPhotos);

    return reviewPhotos;
  }

  public String photoDelete(int reviewPhotoId, HttpSession session) throws Exception {

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인하시기 바랍니다!");
    }

    ReviewPhoto photo = reviewBoardService.getReviewPhoto(reviewPhotoId);
    if (photo == null) {
      throw new Exception("첨부파일 번호가 유효하지 않습니다.");
    }

    Member writer = reviewBoardService.get(photo.getReviewBoardId()).getWriter();
    if (writer.getMemberId() != loginUser.getMemberId()) {
      throw new Exception("권한이 없습니다.");
    }

    reviewBoardService.deleteReviewPhoto(reviewPhotoId);

    storageService.delete(this.bucketName, this.uploadDir, photo.getPhoto());

    return "redirect:../view?no=" + photo.getReviewBoardId();
  }

  @GetMapping("listByCreatedDate")
  public void findByCreatedDate(
      @RequestParam(required = false) String filter,
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "12") int pageSize,
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "0") int regionId, // 원하는 지역 id를 요청
      @RequestParam(defaultValue = "0") int themeId, // 원하는 테마 id를 요청
      Model model) {

    if (pageSize < 3 || pageSize > 20) {
      pageSize = 3;
    }
    if (pageNo < 1) {
      pageNo = 1;
    }

    int numOfPage = 1;
    int numOfRecord = reviewBoardService.countAll(regionId, themeId, filter, keyword);
    numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    model.addAttribute("list", reviewBoardService.findByCreatedDate(pageNo, pageSize, regionId, themeId, filter, keyword));
    model.addAttribute("filter", filter);
    model.addAttribute("keyword", keyword);
    model.addAttribute("regionId", regionId);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
  }

  @GetMapping("listByLikeCount")
  public void findByLikeCount(
      @RequestParam(required = false) String filter,
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "12") int pageSize,
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "0") int regionId, // 원하는 지역 id를 요청
      @RequestParam(defaultValue = "0") int themeId, // 원하는 테마 id를 요청
      Model model) {

    if (pageSize < 3 || pageSize > 20) {
      pageSize = 3;
    }
    if (pageNo < 1) {
      pageNo = 1;
    }

    int numOfPage = 1;
    int numOfRecord = reviewBoardService.countAll(regionId, themeId, filter, keyword);
    numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    model.addAttribute("list", reviewBoardService.findByLikeCount(pageNo, pageSize, regionId, themeId, filter, keyword));
    model.addAttribute("filter", filter);
    model.addAttribute("keyword", keyword);
    model.addAttribute("regionId", regionId);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
  }

  @GetMapping("listByViews")
  public void findByViews(
      @RequestParam(required = false) String filter,
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "12") int pageSize,
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "0") int regionId, // 원하는 지역 id를 요청
      @RequestParam(defaultValue = "0") int themeId, // 원하는 테마 id를 요청
      Model model) {

    if (pageSize < 3 || pageSize > 20) {
      pageSize = 3;
    }
    if (pageNo < 1) {
      pageNo = 1;
    }

    int numOfPage = 1;
    int numOfRecord = reviewBoardService.countAll(regionId, themeId, filter, keyword);
    numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    model.addAttribute("list", reviewBoardService.findByLikeCount(pageNo, pageSize, regionId, themeId, filter, keyword));
    model.addAttribute("filter", filter);
    model.addAttribute("keyword", keyword);
    model.addAttribute("regionId", regionId);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
  }

}
