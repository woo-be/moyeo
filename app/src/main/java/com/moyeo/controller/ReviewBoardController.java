package com.moyeo.controller;

import com.moyeo.service.ReviewBoardService;
import com.moyeo.vo.ReviewBoard;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class ReviewBoardController {
  private final ReviewBoardService boardService;

  @GetMapping("list")
  public void list(Model model){
    model.addAttribute("list", boardService.list());
  }

}
