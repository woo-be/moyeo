package com.moyeo.controller;

import com.moyeo.service.ReviewBoardService;
import com.moyeo.vo.ReviewBoard;
import com.moyeo.vo.ReviewPhoto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class ReviewBoardController {
  private static final Log log = LogFactory.getLog(ReviewBoardController.class);
  private final ReviewBoardService boardService;
  @GetMapping("list")
  public void list(Model model){
    model.addAttribute("list", boardService.list());
  }

}
