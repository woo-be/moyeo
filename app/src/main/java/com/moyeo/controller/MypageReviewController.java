package com.moyeo.controller;

import com.moyeo.service.ReviewBoardService;
import com.moyeo.vo.Member;
import com.moyeo.vo.ReviewBoard;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.ResponseBody;


@RequiredArgsConstructor
@Controller
@RequestMapping("myreview")
public class MypageReviewController {

  private final static Log log = LogFactory.getLog(MypageReviewController.class);
  private final ReviewBoardService reviewBoardService;



  @GetMapping("scrap")
  public void scrapList(
      @RequestParam(required = false, defaultValue = "1")int pageNo,
      @RequestParam(required = false, defaultValue = "8")int pageSize,
      HttpSession session,
      Model model) {

    Member loginUser = (Member) session.getAttribute("loginUser");

    if (pageSize < 8 || pageSize > 20) {
      pageSize = 8;
    }

    if (pageNo < 1){
      pageNo = 1;
    }

    int numOfPage = 1;
    int numOfRecord = reviewBoardService.countScrapByMember(loginUser.getMemberId());

    if (numOfRecord != 0) { // 해당하는 데이터가 하나라도 있다면,
      numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);
    }

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    /* 페이징 페이지 숫자 버튼 */
    int[] pageButtons; // 페이징 페이지 숫자 버튼

    if (numOfPage >= 5) { // a. 게시판 페이지가 5개 이상일 때,
      pageButtons = new int[5]; // 페이지 숫자 버튼의 개수를 5개로 함.

      if (pageNo <= 3) { // 1. 현재 페이지가 시작페이지에서 3페이지 이내의 페이지일 때,
        for (int i = 0; i < 5; i++) { // 숫자 버튼이 1부터 시작하도록 함.
          pageButtons[i] = i + 1;
        }
      } else if (pageNo >= (numOfPage - 2)) { // 2. 현재 페이지가 끝페이지에서 3페이지 이내의 페이지일 때,
        int temp = numOfPage;
        for (int i = 4; i >= 0; i--) { // 숫자 버튼이 끝페이지 -4 부터 시작하도록 함.
          pageButtons[i] = temp--;
        }
      } else { // 3. 그 외의 경우,
        int temp = pageNo - 2;
        for (int i = 0; i < 5; i++) { // 숫자 버튼의 가운데 버튼이 현재페이지를 가리키도록 함.
          pageButtons[i] = temp++;
        }
      }
    } else { // b. 게시판 페이지가 5개 미만일 때,
      pageButtons = new int[numOfPage]; // 페이지 숫자 버튼의 개수를 전체 페이지 개수로 함.
      for (int i = 0; i < numOfPage; i++) { // 숫자 버튼이 1부터 시작하도록 함.
        pageButtons[i] = i + 1;
      }
    }

    List<ReviewBoard> scrapList = reviewBoardService.scrapList(loginUser.getMemberId(), pageNo, pageSize);

    model.addAttribute("scrapList", scrapList);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("numOfPage", numOfPage);
    model.addAttribute("pageButtons", pageButtons); // 페이지 숫자 버튼
    }


  @GetMapping("posted")
  public void reviewList(
      @RequestParam(required = false, defaultValue = "1") int pageNo,
      @RequestParam(required = false, defaultValue = "8") int pageSize,
      HttpSession session,
      Model model) {
    Member loginUser = (Member) session.getAttribute("loginUser");

    if (pageSize < 8 || pageSize > 20) {
      pageSize = 8;
    }
    if (pageNo < 1) {
      pageNo = 1;
    }

    int numOfPage = 1;
    int numOfRecord = reviewBoardService.countPostedByMember(loginUser.getMemberId());

    if (numOfRecord != 0) { // 해당하는 데이터가 하나라도 있다면,
      numOfPage = numOfRecord / pageSize + (numOfRecord % pageSize > 0 ? 1 : 0);
    }

    if (pageNo > numOfPage) {
      pageNo = numOfPage;
    }

    /* 페이징 페이지 숫자 버튼 */
    int[] pageButtons; // 페이징 페이지 숫자 버튼

    if (numOfPage >= 5) { // a. 게시판 페이지가 5개 이상일 때,
      pageButtons = new int[5]; // 페이지 숫자 버튼의 개수를 5개로 함.

      if (pageNo <= 3) { // 1. 현재 페이지가 시작페이지에서 3페이지 이내의 페이지일 때,
        for (int i = 0; i < 5; i++) { // 숫자 버튼이 1부터 시작하도록 함.
          pageButtons[i] = i + 1;
        }
      } else if (pageNo >= (numOfPage - 2)) { // 2. 현재 페이지가 끝페이지에서 3페이지 이내의 페이지일 때,
        int temp = numOfPage;
        for (int i = 4; i >= 0; i--) { // 숫자 버튼이 끝페이지 -4 부터 시작하도록 함.
          pageButtons[i] = temp--;
        }
      } else { // 3. 그 외의 경우,
        int temp = pageNo - 2;
        for (int i = 0; i < 5; i++) { // 숫자 버튼의 가운데 버튼이 현재페이지를 가리키도록 함.
          pageButtons[i] = temp++;
        }
      }
    } else { // b. 게시판 페이지가 5개 미만일 때,
      pageButtons = new int[numOfPage]; // 페이지 숫자 버튼의 개수를 전체 페이지 개수로 함.
      for (int i = 0; i < numOfPage; i++) { // 숫자 버튼이 1부터 시작하도록 함.
        pageButtons[i] = i + 1;
      }
    }

    List<ReviewBoard> reviewList = reviewBoardService.reviewList(loginUser.getMemberId(), pageSize, pageNo);
    for(ReviewBoard reviewBoard : reviewList){
      log.debug(reviewBoard.getLikeCount());
      log.debug(reviewBoard.getViews());
    }
    model.addAttribute("reviewList", reviewList);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("numOfPage", numOfPage);
    model.addAttribute("pageButtons", pageButtons); // 페이지 숫자 버튼
  }
}