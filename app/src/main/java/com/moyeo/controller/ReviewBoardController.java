package com.moyeo.controller;

import com.moyeo.service.ReviewBoardService;
import com.moyeo.service.StorageService;
import com.moyeo.vo.ReviewBoard;
import com.moyeo.vo.ReviewPhoto;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class ReviewBoardController {

  private final ReviewBoardService reviewBoardService;
  private final StorageService storageService;
  private String uploadDir = "review/";

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
  public void list(Model model) {
    model.addAttribute("list", reviewBoardService.list());
  }

}
