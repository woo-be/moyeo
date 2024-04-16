package com.moyeo.controller;

import java.beans.PropertyEditorSupport;
import java.sql.Date;
import javax.servlet.http.HttpSession;
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
  public ModelAndView exceptionHandler(Exception e, HttpSession session) {
    ModelAndView mv = new ModelAndView();

    mv.addObject("message", session.getAttribute("message"));
    mv.addObject("replaceUrl", session.getAttribute("replaceUrl"));

    mv.setViewName("error");
    return mv;
  }
}
