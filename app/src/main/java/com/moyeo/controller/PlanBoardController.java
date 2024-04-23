package com.moyeo.controller;

import com.amazonaws.services.s3.internal.eventstreaming.Message;
import com.moyeo.dao.PlanBoardDao;
import com.moyeo.service.MessageService;
import com.moyeo.service.PlanBoardService;
import com.moyeo.service.StorageService;
import com.moyeo.vo.Member;

import com.moyeo.vo.Msg;
import com.moyeo.vo.PlanBoard;
import com.moyeo.vo.PlanPhoto;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/plan")
@RequiredArgsConstructor
@Controller
public class PlanBoardController {
  private static final Log log = LogFactory.getLog(PlanBoardController.class);
  private final PlanBoardService planBoardService;
  private final StorageService storageService;
  private final String uploadDir = "plan/";

  @Value("${ncp.ss.bucketname}")
  private String bucketName;

  @GetMapping("list")
  public void list(
      int recruitBoardId,
      Model model) {
    List<PlanBoard> list;
    list = planBoardService.list(recruitBoardId);

    log.debug("planBoard = " + list);
    model.addAttribute("list", list);
    model.addAttribute("recruitBoardId", recruitBoardId);
  }

  @GetMapping("view")
  public void view(int planBoardId, Model model) {
    model.addAttribute("planBoard", planBoardService.get(planBoardId));
  }


  @GetMapping("form")
  public void form(
      int recruitBoardId,
      Model model) throws Exception {

    model.addAttribute("recruitBoardId", recruitBoardId);

  }

  @PostMapping("add")
  public String add(
      PlanBoard planBoard,
      HttpSession session,
      SessionStatus sessionStatus,
      Model model) throws Exception {

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      session.setAttribute("message", "로그인 해주세요");
      session.setAttribute("replaceUrl", "/auth/form");
    }

    List<PlanPhoto> photos = (List<PlanPhoto>) session.getAttribute("photos");
    if (photos == null) {
      photos = new ArrayList<>();
    }

    for (int i = photos.size() - 1; i >=0; i--) {
      PlanPhoto planPhoto = photos.get(i);
      if (planBoard.getContent().indexOf(planPhoto.getPhoto()) == -1) {
        storageService.delete(this.bucketName, this.uploadDir, planPhoto.getPhoto());
        photos.remove(i);
      }
      if (photos.size() > 0) {
        planBoard.setPhotos(photos);
      }
    }

    model.addAttribute("recruitBoardId", planBoard.getRecruitBoardId());


    planBoardService.add(planBoard);

    sessionStatus.setComplete();

    return "redirect:view?planBoardId=" + planBoard.getPlanBoardId();
  }

  @PostMapping("update")
  public String update(
      PlanBoard planBoard,
      HttpSession session,
      SessionStatus sessionStatus,
      Model model) throws Exception {

    model.addAttribute("recruitBoardId", planBoard.getRecruitBoardId());

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      session.setAttribute("message", "로그인 해주세요");
      session.setAttribute("replaceUrl", "/auth/form");
    }

    PlanBoard old = planBoardService.get(planBoard.getPlanBoardId());
    old.setPhotos(planBoardService.getPhotos(planBoard.getPlanBoardId()));
    if (old == null) {
      throw new Exception("번호가 유효하지 않습니다");
    }

    List<PlanPhoto> photos = (List<PlanPhoto>) session.getAttribute("photos");
    if (photos == null) {
      photos = new ArrayList<>();
    }

    if (old.getPhotos().size() > 0) {
      photos.addAll(old.getPhotos());
    }
    if (photos != null) {
      for (int i = photos.size() - 1; i >= 0; i--) {
        PlanPhoto planPhoto = photos.get(i);
        if (planBoard.getContent().indexOf(planPhoto.getPhoto()) == -1) {
          storageService.delete(this.bucketName, this.uploadDir, planPhoto.getPhoto());
          photos.remove(i);
        }
      }
      if (photos.size() > 0) {
        planBoard.setPhotos(photos);
      }
    }
    planBoardService.update(planBoard);

    sessionStatus.setComplete();

    return "redirect:view?planBoardId=" + planBoard.getPlanBoardId();
  }

  @PostMapping("updateForm")
  public void updateForm(int recruitBoardId, int planBoardId, HttpSession session, Model model) {

    PlanBoard planBoard = planBoardService.get(planBoardId);

    model.addAttribute("recruitBoardId", recruitBoardId);
    model.addAttribute("updatePlanBoard", planBoard);

  }

  @GetMapping("delete")
  public String delete(
      @Param("planBoardId") int planBoardId,
      @Param("recruitBoardId") int recruitBoardId) throws Exception {

    List<PlanPhoto> photos = planBoardService.getPhotos(planBoardId);

    planBoardService.delete(planBoardId, recruitBoardId);

    for (PlanPhoto photo : photos) {
      storageService.delete(this.bucketName, this.uploadDir, photo.getPhoto());

    }

    return "redirect:/plan/list?recruitBoardId=" + recruitBoardId;
  }

  @PostMapping("photo/upload")
  @ResponseBody
  public Object photoUpload(
      MultipartFile[] photos,
      HttpSession session,
      Model model) throws Exception {

    ArrayList<PlanPhoto> planPhotos = new ArrayList<>();

    for (MultipartFile photo : photos) {
      if (photo.getSize() == 0) {
        continue;
      }
      String photoName = storageService.upload(this.bucketName, this.uploadDir, photo);
      planPhotos.add(PlanPhoto.builder().photo(photoName).build());
    }

    model.addAttribute("planPhotos", planPhotos);

    return planPhotos;
  }

  @GetMapping("photo/delete")
  public String photoDelete(int planPhotoId, HttpSession session) throws Exception {

    PlanPhoto planPhoto = planBoardService.getPhoto(planPhotoId);

    planBoardService.deletePlanPhoto(planPhotoId);

    storageService.delete(this.bucketName, this.uploadDir, planPhoto.getPhoto());

    return "redirect:../view?no=" + planPhoto.getPlanPhotoId();
  }

//  @GetMapping("menu")
//  public int menu(
//      int recruitBoardId,
//      Model model
//  ) throws Exception {
//
//    model.addAttribute("recruitBoardId", recruitBoardId);
//
//  }
}
