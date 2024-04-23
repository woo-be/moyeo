package com.moyeo.controller;

import com.moyeo.vo.MoyeoError;
import java.beans.PropertyEditorSupport;
import java.sql.Date;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalControllerAdvice {

  @InitBinder
  public void initBinder(WebDataBinder webDataBinder) {
    webDataBinder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
      public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(Date.valueOf(text));
      }
    });
  }

  @ExceptionHandler
  public ModelAndView exceptionHandler(MoyeoError e) {
    ModelAndView mv = new ModelAndView();

    mv.addObject("message", e.getMsg());
    mv.addObject("replaceUrl", e.getUrl());

    mv.setViewName("error");
    return mv;
  }
}
