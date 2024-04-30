package com.moyeo.controller;


import com.moyeo.service.PlanBoardService;
import com.moyeo.service.RecruitBoardService;
import com.moyeo.service.StorageService;
import com.moyeo.vo.ErrorName;
import com.moyeo.vo.Member;
import com.moyeo.vo.MoyeoError;
import com.moyeo.vo.Pin;
import com.moyeo.vo.PlanBoard;
import com.moyeo.vo.PlanPhoto;
import com.moyeo.vo.RecruitBoard;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
  private final RecruitBoardService recruitBoardService;
  private final String uploadDir = "plan/";

  @Value("${ncp.ss.bucketname}")
  private String bucketName;

  @GetMapping("list")
  @ResponseBody
  public List<Pin> list(
      int recruitBoardId,
      String tripDate,
      Model model) {
    List<Pin> list;
    list = planBoardService.pinList(recruitBoardId, tripDate);

    log.debug("planBoard = " + list);
//    model.addAttribute("list", list);
//    model.addAttribute("recruitBoardId", recruitBoardId);
    return list;
  }

  @GetMapping("view")
  @ResponseBody
  public PlanBoard view(int recruitBoardId, String tripDate, double latitude, double longitude) {
    PlanBoard planboard = planBoardService.get(recruitBoardId, tripDate, latitude, longitude);
    log.debug(planboard);
    return planboard;
  }




  @GetMapping("form")
  public void form(
      int recruitBoardId,
      Model model) throws Exception {

    RecruitBoard recruitBoard = recruitBoardService.get(recruitBoardId);
    Date startDate = recruitBoard.getStartDate();
    Date endDate = recruitBoard.getEndDate();

    log.debug(startDate);
    log.debug(endDate);
    model.addAttribute("recruitBoardId", recruitBoardId);
    model.addAttribute("startDate", startDate);
    model.addAttribute("endDate", endDate);

  }

  @PostMapping("add")
  @ResponseBody
  public String add(
      @RequestParam("tripOrder") String tripOrder,
      @RequestParam("title") String title,
      @RequestParam("content") String content,
      @RequestParam("recruitBoardId") String recruitBoardId,
      @RequestParam("tripDate") String tripDate,
      @RequestParam("latitude") String latitude,
      @RequestParam("longitude") String longitude,
      HttpSession session,
      SessionStatus sessionStatus,
      Model model) throws Exception {

    // 일정 객체를 만든다.
    PlanBoard planBoard = PlanBoard.builder().
        tripOrder(Integer.parseInt(tripOrder)).
        title(title).
        content(content).
        recruitBoardId(Integer.parseInt(recruitBoardId)).
        tripDate(Date.valueOf(tripDate)).
        latitude(Double.parseDouble(latitude)).
        longitude(Double.parseDouble(longitude)).
        build();

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError("로그인이 필요합니다.","/auth/form");
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

    log.debug(planBoard);

    planBoardService.add(planBoard);

    sessionStatus.setComplete();

//    return "redirect:view?planBoardId=" + planBoard.getPlanBoardId();
    return "일정 등록 했습니다.";
  }

  @PostMapping("update")
  @ResponseBody
  public String update(
      @RequestParam("planBoardId") String planBoardId,
      @RequestParam("viewTripOrder") String viewTripOrder,
      @RequestParam("viewPostTitle") String viewPostTitle,
      @RequestParam("viewPostContent") String viewPostContent,
      HttpSession session,
      SessionStatus sessionStatus,
      Model model) throws Exception {

    log.debug("planBoardId: " + planBoardId);
    log.debug("viewTripOrder: " + viewTripOrder);
    log.debug("viewPostTitle: " + viewPostTitle);
    log.debug("viewPostContent: " + viewPostContent);

    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new MoyeoError(ErrorName.LOGIN_REQUIRED, "/auth/form");
    }

    PlanBoard old = planBoardService.get(Integer.parseInt(planBoardId));
    PlanBoard planBoard = PlanBoard.builder()
        .planBoardId(old.getPlanBoardId())
        .tripOrder(Integer.parseInt(viewTripOrder))
        .title(viewPostTitle)
        .content(viewPostContent)
        .recruitBoardId(old.getRecruitBoardId())
        .tripDate(old.getTripDate())
        .latitude(old.getLatitude())
        .longitude(old.getLongitude())
        .build();

    log.debug("old:" + old);
    log.debug("planBoard:" + planBoard);

    old.setPhotos(planBoardService.getPhotos(planBoard.getPlanBoardId()));
//    log.debug(old.getPhotos().getFirst().getPhoto());

    if (old == null) {
      throw new Exception(ErrorName.INVALID_NUMBER);
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

    model.addAttribute("recruitBoardId", planBoard.getRecruitBoardId());

    planBoardService.update(planBoard);

    sessionStatus.setComplete();

    return "일정을 수정했습니다.";
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

  @GetMapping("findByTripDate")
  public void findByTripDate(
      @Param("tripDate")String tripDate,
      @Param("recruitBoardId")int recruitBoardId,
      Model model) {
    List<PlanBoard> list;
    list = planBoardService.findByTripDate(tripDate, recruitBoardId);

    model.addAttribute("tripDate", tripDate);
    model.addAttribute("recruitBoardId", recruitBoardId);
    model.addAttribute("list", list);
log.debug(String.format("recruitBoardId =%s", recruitBoardId));
    log.debug(String.format("recruitBoardId =%s", tripDate));
log.debug(list);
  }

  @GetMapping("planBoardList")
  public void planBoardList(int recruitBoardId, String date, Model model, HttpSession session) {
    Member loginUser = (Member) session.getAttribute("loginUser");
    log.debug(loginUser);
    model.addAttribute("recruitBoardId", recruitBoardId);
    model.addAttribute("date", date);
    model.addAttribute("nickname", loginUser.getNickname());
  }

}
