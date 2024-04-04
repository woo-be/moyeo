package com.moyeo.controller;

import com.moyeo.service.ReviewBoardService;
import com.moyeo.service.ReviewCommentService;
import com.moyeo.service.StorageService;
import com.moyeo.vo.ReviewBoard;
import com.moyeo.vo.ReviewComment;
import com.moyeo.vo.ReviewPhoto;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
      Model model) {
    if (pageSize < 3 || pageSize > 20) {
      pageSize = 3;
    }
    if (pageNo < 1) {
      pageNo = 1;
    }
    int numOfRecord = reviewBoardService.countAll();
    int numOfPage = numOfRecord / pageSize + ((numOfRecord % pageSize) > 0 ? 1 : 0);

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }
    model.addAttribute("list", reviewBoardService.list(pageNo, pageSize));
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
  }

  @GetMapping("view")
  public void reviewBoardGet(int reviewBoardId, Model model) {
    model.addAttribute("reviewBoard", reviewBoardService.get(reviewBoardId));
  }

  @GetMapping("delete")
  public String delete(
      int reviewBoardId) throws Exception {

    reviewBoardService.delete(reviewBoardId);
    return "redirect:list";
  }
}
