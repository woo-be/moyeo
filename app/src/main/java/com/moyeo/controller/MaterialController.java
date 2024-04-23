//package com.moyeo.controller;
//
//import com.moyeo.service.MaterialService;
//import com.moyeo.service.StorageService;
//import com.moyeo.vo.Material;
//import com.moyeo.vo.MaterialPhoto;
//import com.moyeo.vo.Member;
//import java.util.ArrayList;
//import java.util.List;
//import javax.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.support.SessionStatus;
//import org.springframework.web.multipart.MultipartFile;
//
//@RequestMapping("/material")
//@RequiredArgsConstructor
//@Controller
//public class MaterialController {
//  private static final Log log = LogFactory.getLog(MaterialController.class);
//  private final MaterialService materialService;
//  private final StorageService storageService;
//  private final String uploadDir = "material/";
//
//  @Value("${ncp.ss.bucketname}")
//  private String bucketName;
//
//  @GetMapping("list")
//  public void list(
//      int recruitBoardId,
//      Model model) {
//    List<Material> list;
//    list = materialService.list(recruitBoardId);
//
//    model.addAttribute("list", list);
//    model.addAttribute("recruitBoardId", recruitBoardId);
//  }
//
//  @GetMapping("view")
//  public void view(int materialId, Model model) {
//    model.addAttribute("material", materialService.get(materialId));
//  }
//
//  @GetMapping("form")
//  public void form(
//      int recruitBoardId,
//      Model model) throws Exception {
//
//    model.addAttribute("recruitBoardId", recruitBoardId);
//  }
//
//  @PostMapping("add")
//  public String add(
//      Material material,
//      HttpSession session,
//      SessionStatus sessionStatus,
//      Model model) throws Exception {
//
//    Member loginUser = (Member) session.getAttribute("loginUser");
//    if (loginUser == null) {
//      session.setAttribute("message", "로그인 해주세요");
//      session.setAttribute("replaceUrl", "/auth/form");
//    }
//
//    List<MaterialPhoto> materialPhotos = (List<MaterialPhoto>) session.getAttribute("materialPhotos");
//
//    if (materialPhotos != null) {
//      for (int i = materialPhotos.size() -1; i >=0; i--) {
//        MaterialPhoto materialPhoto = materialPhotos.get(i);
//        if (material.getContent().indexOf(materialPhoto.getMaterialPhoto()) == -1) {
//          storageService.delete(this.bucketName, this.uploadDir, materialPhoto.getMaterialPhoto());
//          materialPhotos.remove(i);
//        }
//      }
//      if (materialPhotos.size() > 0) {
//        material.setPhotos(materialPhotos);
//      }
//    }
//
//    model.addAttribute("recruitBoardId", material.getRecruitBoardId());
//
//    materialService.add(material);
//
//    sessionStatus.setComplete();
//
//    return "redirect:view?materialId=" + material.getMaterialId();
//  }
//
//  @PostMapping("update")
//  public String update(
//      Material material,
//      HttpSession session,
//      Model model) throws Exception {
//
//    model.addAttribute("recruitBoardId", material.getRecruitBoardId());
//
//    Member loginUser = (Member) session.getAttribute("loginUser");
//    if (loginUser == null) {
//      session.setAttribute("message", "로그인 해주세요");
//      session.setAttribute("replaceUrl", "/auth/form");
//    }
//
//    Material old = materialService.get(material.getMaterialId());
//    if (old == null) {
//      throw new Exception("번호가 유효하지 않습니다.");
//    }
//
//    materialService.update(material);
//
//    return "redirect:view?materialId=" + material.getMaterialId();
//  }
//
//  @PostMapping("updateForm")
//  public void updateForm(int recruitBoardId, int materialId, HttpSession session, Model model) {
//
//    Material material = materialService.get(materialId);
//
//    model.addAttribute("recruitBoardId", recruitBoardId);
//    model.addAttribute("updateMaterial", material);
//  }
//
//  @GetMapping("delete")
//  public String delete(int materialId) throws Exception {
//    materialService.delete(materialId);
//    return "redirect:list";
//  }
//
//  @PostMapping("photo/upload")
//  @ResponseBody
//  public Object photoUpload(MultipartFile[] photos, HttpSession session, Model model)
//    throws Exception {
//
//    ArrayList<MaterialPhoto> materialPhotos = new ArrayList<>();
//
//    for (MultipartFile photo : photos) {
//      if (photo.getSize() == 0) {
//        continue;
//      }
//      String photoName = storageService.upload(this.bucketName, this.uploadDir, photo);
//      materialPhotos.add(MaterialPhoto.builder().materialPhoto(photoName).build());
//    }
////    MaterialPhoto mp = new MaterialPhoto();
////    mp.setMaterialPhoto(photoName);
//
//    model.addAttribute("materialPhotos", materialPhotos);
//
//    return materialPhotos;
//  }
//}
