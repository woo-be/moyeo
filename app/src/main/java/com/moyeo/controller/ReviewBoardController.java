package com.moyeo.controller;

import com.moyeo.service.RegionService;
import com.moyeo.service.ReviewBoardService;
import com.moyeo.service.ReviewCommentService;
import com.moyeo.service.StorageService;
import com.moyeo.service.ThemeService;
import com.moyeo.vo.Member;
import com.moyeo.vo.ReviewBoard;
import com.moyeo.vo.ReviewPhoto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
  private final String uploadDir = "review/";

  @Value("${ncp.ss.bucketname}")
  private String bucketName;

  @GetMapping("form")
  public void form(Model model) throws Exception {
    model.addAttribute("regionId", 0);
    model.addAttribute("themeId", 0);
  }

  @PostMapping("add")
  public String add(
      ReviewBoard reviewBoard,
      HttpSession session,
      SessionStatus sessionStatus,
      Model model) throws Exception {

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      session.setAttribute("message", "로그인 해주세요");
      session.setAttribute("replaceUrl", "/auth/form");
    }

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
      /*@RequestParam(required = false) int themeId,*/
      Model model) throws Exception {
    if (pageSize < 3 || pageSize > 20) {
      pageSize = 3;
    }
    if (pageNo < 1) {
      pageNo = 1;
    }

    int numOfPage = 1;
    int numOfRecord = reviewBoardService.countAll(regionId, filter, keyword);
    numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    // list 메서드에 필요한 모든 값을 넘기고 mapper의 mybatis로 조건문 처리.
    model.addAttribute("list", reviewBoardService.list(pageNo, pageSize, regionId, filter, keyword));

    model.addAttribute("filter", filter);
    model.addAttribute("keyword", keyword);
    model.addAttribute("regionId", regionId);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
  }

  @GetMapping("view")
  public void reviewBoardGet(int reviewBoardId, Model model) {
    ReviewBoard reviewBoard = reviewBoardService.get(reviewBoardId);
    log.debug(String.format("%s==================================\n", reviewBoard.getAddress()));
    if(reviewBoard.getAddress() == null){
      reviewBoard.setAddress("서울특별시 용산구 한강대로 405");
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
  public String delete(
      int reviewBoardId) throws Exception {

    reviewBoardService.delete(reviewBoardId);
    return "redirect:list";
  }

  @PostMapping("update")
  public String update(
      ReviewBoard reviewBoard,
      HttpSession session,
      Model model,
      SessionStatus sessionStatus) throws Exception {
//    model.addAttribute("updateReviewBoard", reviewBoard);

//    Member loginUser = (Member) session.getAttribute("loginUser");
//    if (loginUser == null) {
//      throw new Exception("로그인하시기 바랍니다!");
//    }

    ReviewBoard old = reviewBoardService.get(reviewBoard.getReviewBoardId());
    log.debug(old);
//    if (old == null) {
//      throw new Exception("번호가 유효하지 않습니다.");
//    } else if (old.getWriter().getMemberId() != loginUser.getMemberId()) {
//      throw new Exception("권한이 없습니다.");
//    }

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

    log.debug(String.format("%d      %s        %s~~~~~~~~~~~~~~~~~~~~~~~~~~~",
        reviewBoard.getReviewBoardId(), reviewBoard.getTitle(), reviewBoard.getContent()));

    reviewBoardService.update(reviewBoard);

    sessionStatus.setComplete();

//    log.debug(String.format("%d      %s        %s~~~~~~~~~~~~~~~~~~~~~~~~~~~",reviewBoard.getReviewBoardId(), reviewBoard.getTitle(), reviewBoard.getContent()));
//    reviewBoardService.update(reviewBoard);
    return "redirect:view?reviewBoardId=" + reviewBoard.getReviewBoardId();
  }

  @PostMapping("updateForm")
  public void updateForm(ReviewBoard reviewBoard, Model model) {
    model.addAttribute("updateReviewBoard", reviewBoard);
    log.debug(String.format("%d      %s        %s       %d~~~~~~~~~~~~~~~~~~~~~~~~~~~",
        reviewBoard.getReviewBoardId(), reviewBoard.getTitle(), reviewBoard.getContent(),
        reviewBoard.getThemeId()));
    model.addAttribute("regionId", reviewBoard.getRegionId());
    model.addAttribute("themeId", reviewBoard.getThemeId());
  }


  @PostMapping("photo/upload")
  @ResponseBody
  public Object photoUpload(MultipartFile[] photos, HttpSession session, Model model)
      throws Exception {

    ArrayList<ReviewPhoto> reviewPhotos = new ArrayList<>();

//    Member loginUser = (Member) session.getAttribute("loginUser");
//    if (loginUser == null) {
//      return reviewPhotos;
//    }

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

}
