package com.moyeo.controller;

import com.moyeo.service.RecruitBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("recruit")
public class RecruitBoardController {

  private final RecruitBoardService recruitBoardService;

  @GetMapping("list")
  public void list(Model model) {
    model.addAttribute("list", recruitBoardService.list());
  }

  public void add() {
  }

  public void form() {
  }

  public void view() {
  }

  public void update() {
  }

  public void delete() {
  }

}
