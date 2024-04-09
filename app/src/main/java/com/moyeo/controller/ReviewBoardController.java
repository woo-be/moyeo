package com.moyeo.controller;

import com.moyeo.service.ReviewBoardService;
import com.moyeo.service.ReviewCommentService;
import com.moyeo.service.StorageService;
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
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class ReviewBoardController {

  private static final Log log = LogFactory.getLog(ReviewBoardController.class);
  private final ReviewBoardService reviewBoardService;
  private final StorageService storageService;
  private final ReviewCommentService reviewCommentService;
  private final String uploadDir = "review/";

  @Value("${ncp.ss.bucketname}")
  private String bucketName;

  @GetMapping("form")
  public void form() throws Exception {
  }

  @PostMapping("add")
  public String add(
      ReviewBoard reviewBoard,
      MultipartFile[] reviewPhotos,
      HttpSession session,
      Model model) throws Exception {

//    Member loginUser = (Member) session.getAttribute("loginUser");
//    if (loginUser == null) {
//      throw new Exception("로그인하시기 바랍니다!");
//    }
//    reviewBoard.setWriter(loginUser);

    ArrayList<ReviewPhoto> photos = new ArrayList<>();
    for (MultipartFile file : reviewPhotos) {
      if (file.getSize() == 0) {
        continue;
      }
      String filename = storageService.upload(this.bucketName, this.uploadDir, file);
      photos.add(ReviewPhoto.builder().photo(filename).build());
    }

    if (photos.size() > 0) {
      reviewBoard.setPhotos(photos);
    }

    reviewBoardService.add(reviewBoard);

    return "redirect:list";
  }


  @GetMapping("list")
  public void list(
      @RequestParam(defaultValue = "6") int pageSize,
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "0", required = false) int regionId, // 원하는 지역 id를 요청
      /*@RequestParam(required = false) int themeId,*/
      Model model) {
    if (pageSize < 3 || pageSize > 20) {
      pageSize = 3;
    }
    if (pageNo < 1) {
      pageNo = 1;
    }

    // 사용자가 원하는 지역 또는 테마를 경우의 수로 나눠서 후기 개수를 카운트
    // 메소드 파라미터만 달라지기 때문에 오버로딩 해서 구현
    int numOfRecord = 0;
    if (regionId == 0 /*&& themeId == 0*/) {
      numOfRecord = reviewBoardService.countAll();
    } else if (regionId != 0) {
      numOfRecord = reviewBoardService.countAll(regionId);
    /*} else if (themeId!=0) {
      numOfRecord = reviewBoardService.countAll(themeId);
    } else if (regionId!=0 && themeId!=0) {
      numOfRecord = reviewBoardService.countAll(regionId, themeId);*/
    }

    int numOfPage = numOfRecord / pageSize + ((numOfRecord % pageSize) >= 0 ? 1 : 0);

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    // 사용자가 원하는 지역 또는 테마를 경우의 수로 나눠서 후기 리스트를 가져온다.
    // 메소드 파라미터만 달라지기 때문에 오버로딩 해서 구현
    List<ReviewBoard> list;
    if (regionId == 0 /*&& themeId == 0*/) {
      list = reviewBoardService.list(pageNo, pageSize);
      model.addAttribute("list", list);
    } else if (regionId != 0) {
      list = reviewBoardService.list(pageNo, pageSize, regionId);
      model.addAttribute("list", list);
    /*} else if (themeId!=0) {
      model.addAttribute("list", reviewBoardService.list(pageNo, pageSize, themeId));
    } else if (regionId!=0 && themeId!=0) {
     model.addAttribute("list", reviewBoardService.list(pageNo, pageSize, regionId, themeId));*/
    }

    model.addAttribute("regionId", regionId);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
  }

  @GetMapping("view")
  public void reviewBoardGet(int reviewBoardId, Model model) {
    model.addAttribute("reviewBoard", reviewBoardService.get(reviewBoardId));
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
      MultipartFile[] reviewPhotos,
      HttpSession session,
      Model model) throws Exception {
//    model.addAttribute("updateReviewBoard", reviewBoard);

//    Member loginUser = (Member) session.getAttribute("loginUser");
//    if (loginUser == null) {
//      throw new Exception("로그인하시기 바랍니다!");
//    }
//
//    ReviewBoard old = reviewBoardService.get(reviewBoard.getReviewBoardId());
//    if (old == null) {
//      throw new Exception("번호가 유효하지 않습니다.");
//    } else if (old.getWriter().getMemberId() != loginUser.getMemberId()) {
//      throw new Exception("권한이 없습니다.");
//    }
    log.debug(String.format("%d      %s        %s~~~~~~~~~~~~~~~~~~~~~~~~~~~",
        reviewBoard.getReviewBoardId(), reviewBoard.getTitle(), reviewBoard.getContent()));
    reviewBoardService.update(reviewBoard);
    return "redirect:list";
  }

  @PostMapping("updateForm")
  public void updateForm(ReviewBoard reviewBoard, Model model) {
    model.addAttribute("updateReviewBoard", reviewBoard);
    log.debug(String.format("%d      %s        %s~~~~~~~~~~~~~~~~~~~~~~~~~~~",
        reviewBoard.getReviewBoardId(), reviewBoard.getTitle(), reviewBoard.getContent()));
  }

}
